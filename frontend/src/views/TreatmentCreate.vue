<template>
  <div class="treatment-create">
    <van-nav-bar title="单次调理记录" left-arrow @click-left="$router.back()" />

    <van-form @submit="onSubmit" class="form-container">
      <van-cell-group inset title="居民信息">
        <van-field
          v-model="residentDisplay"
          name="residentId"
          label="选择居民"
          placeholder="请点击选择居民"
          readonly
          is-link
          @click="showPicker = true"
          :rules="[{ required: true, message: '请选择居民' }]"
        />
      </van-cell-group>

      <van-cell-group inset title="调理信息" style="margin-top: 12px">
        <van-field
          v-model="form.treatmentDate"
          name="treatmentDate"
          label="调理日期"
          placeholder="请选择日期"
          readonly
          is-link
          @click="showDatePicker = true"
          :rules="[{ required: true, message: '请选择调理日期' }]"
        />
        <van-field
          v-model="form.operatorName"
          name="operatorName"
          label="操作人"
          placeholder="请输入操作人姓名"
        />
      </van-cell-group>

      <van-cell-group inset title="评估信息" style="margin-top: 12px">
        <van-cell title="当前疼痛评分" class="pain-cell">
          <template #value>
            <PainScoreSlider v-model="form.currentPainScore" />
          </template>
        </van-cell>
        <van-field name="sleepChange" label="睡眠质量变化">
          <template #input>
            <van-radio-group v-model="form.sleepChange">
              <van-cell
                v-for="item in sleepOptions"
                :key="item.value"
                :title="item.text"
                clickable
                @click="form.sleepChange = item.value"
              >
                <template #right-icon>
                  <van-radio :name="item.value" />
                </template>
              </van-cell>
            </van-radio-group>
          </template>
        </van-field>
      </van-cell-group>

      <div class="submit-btn">
        <van-button round block type="primary" native-type="submit" :loading="submitting">
          提交记录
        </van-button>
      </div>
    </van-form>

    <van-popup v-model:show="showPicker" position="bottom" round>
      <van-picker
        :columns="residentOptions"
        @confirm="onResidentConfirm"
        @cancel="showPicker = false"
      />
    </van-popup>

    <van-popup v-model:show="showDatePicker" position="bottom" round>
      <van-date-picker
        v-model="currentDate"
        title="选择日期"
        @confirm="onDateConfirm"
        @cancel="showDatePicker = false"
      />
    </van-popup>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast } from 'vant'
import { createRecord } from '@/api/treatment'
import { getResident } from '@/api/resident'
import PainScoreSlider from '@/components/PainScoreSlider.vue'
import { SLEEP_MAP } from '@/utils/constants'
import dayjs from 'dayjs'
import { useResidentStore } from '@/stores/resident'
import { useTreatmentStore } from '@/stores/treatment'

const router = useRouter()
const route = useRoute()
const residentStore = useResidentStore()
const treatmentStore = useTreatmentStore()
const submitting = ref(false)
const showPicker = ref(false)
const showDatePicker = ref(false)

const form = ref({
  residentId: null,
  treatmentDate: dayjs().format('YYYY-MM-DD'),
  currentPainScore: 0,
  sleepChange: null,
  operatorName: ''
})

const currentDate = ref(dayjs().format('YYYY-MM-DD').split('-'))

const residentOptions = ref([])
const residentDisplay = ref('')

const sleepOptions = Object.entries(SLEEP_MAP).map(([value, text]) => ({
  value: Number(value),
  text
}))

function onResidentConfirm({ selectedOptions }) {
  const item = selectedOptions[0]
  form.value.residentId = item.value
  residentDisplay.value = item.text
  showPicker.value = false
}

function onDateConfirm({ selectedValues }) {
  form.value.treatmentDate = selectedValues.join('-')
  showDatePicker.value = false
}

async function loadResidentOptions() {
  const residentIdFromQuery = route.query.residentId
  if (residentIdFromQuery) {
    try {
      const resident = await getResident(residentIdFromQuery)
      residentOptions.value = [{ text: `${resident.name} (${resident.age}岁)`, value: resident.id }]
      form.value.residentId = resident.id
      residentDisplay.value = `${resident.name} (${resident.age}岁)`
      residentStore.setCurrentResident(resident)
      return
    } catch (e) {
      console.error(e)
    }
  }

  const stored = localStorage.getItem('recentResidents')
  if (stored) {
    const residents = JSON.parse(stored)
    residentOptions.value = residents.map(r => ({
      text: `${r.name} (${r.age}岁)`,
      value: r.id
    }))
  }
  if (residentStore.currentResident) {
    const r = residentStore.currentResident
    form.value.residentId = r.id
    residentDisplay.value = `${r.name} (${r.age}岁)`
  }
}

async function onSubmit() {
  if (!form.value.residentId) {
    showToast('请选择居民')
    return
  }
  if (!form.value.treatmentDate) {
    showToast('请选择调理日期')
    return
  }
  submitting.value = true
  try {
    const data = { ...form.value }
    if (!data.operatorName) delete data.operatorName
    const result = await createRecord(data)
    treatmentStore.addRecord(result)
    showToast({ type: 'success', message: '记录提交成功' })
    router.push(`/treatment/list/${data.residentId}`)
  } catch (e) {
    console.error(e)
  } finally {
    submitting.value = false
  }
}

onMounted(loadResidentOptions)
</script>

<style scoped>
.treatment-create {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 24px;
}

.form-container {
  padding: 12px 0;
}

.pain-cell {
  padding: 8px 16px;
}

.submit-btn {
  padding: 24px 16px 0;
}
</style>
