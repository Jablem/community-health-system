<template>
  <div class="daily-view">
    <van-nav-bar title="预约与健康自评" left-arrow @click-left="$router.back()" />

    <van-tabs v-model:active="activeTab" sticky>
      <van-tab title="健康自评" name="self">
        <van-form @submit="onSelfAssessSubmit" class="form-container">
          <van-cell-group inset title="今日自评">
            <van-field name="residentId" label="选择居民">
              <template #input>
                <van-radio-group v-model="selfForm.residentId">
                  <van-cell
                    v-for="item in residentOptions"
                    :key="item.value"
                    :title="item.text"
                    clickable
                    @click="selfForm.residentId = item.value"
                  >
                    <template #right-icon>
                      <van-radio :name="item.value" />
                    </template>
                  </van-cell>
                </van-radio-group>
              </template>
            </van-field>
            <van-cell title="今日疼痛评分" class="pain-cell">
              <template #value>
                <PainScoreSlider v-model="selfForm.painScore" />
              </template>
            </van-cell>
            <van-field
              v-model="selfForm.feeling"
              label="今日感受"
              type="textarea"
              rows="3"
              placeholder="请描述今天的身体状况和感受"
              autosize
            />
            <van-field
              v-model="selfForm.sleepQuality"
              label="昨晚睡眠"
              placeholder="好/一般/差"
            />
          </van-cell-group>

          <div class="submit-btn">
            <van-button round block type="primary" native-type="submit">
              提交自评
            </van-button>
          </div>
        </van-form>
      </van-tab>

      <van-tab title="预约调理" name="appointment">
        <van-form @submit="onAppointmentSubmit" class="form-container">
          <van-cell-group inset title="预约信息">
            <van-field name="residentId" label="选择居民">
              <template #input>
                <van-radio-group v-model="aptForm.residentId">
                  <van-cell
                    v-for="item in residentOptions"
                    :key="item.value"
                    :title="item.text"
                    clickable
                    @click="aptForm.residentId = item.value"
                  >
                    <template #right-icon>
                      <van-radio :name="item.value" />
                    </template>
                  </van-cell>
                </van-radio-group>
              </template>
            </van-field>
            <van-field
              v-model="aptForm.date"
              label="预约日期"
              placeholder="请选择日期"
              readonly
              is-link
              @click="showDatePicker = true"
            />
            <van-field
              v-model="aptForm.timeSlot"
              label="时间段"
              placeholder="请选择时间段"
              readonly
              is-link
              @click="showTimePicker = true"
            />
            <van-field
              v-model="aptForm.remark"
              label="备注"
              type="textarea"
              rows="2"
              placeholder="如有特殊需求请备注"
              autosize
            />
          </van-cell-group>

          <div class="submit-btn">
            <van-button round block type="primary" native-type="submit">
              提交预约
            </van-button>
          </div>
        </van-form>

        <van-cell-group inset title="我的预约" style="margin-top: 12px">
          <van-empty v-if="appointments.length === 0" description="暂无预约记录" />
          <van-cell
            v-for="apt in appointments"
            :key="apt.id"
            :title="apt.date"
            :value="apt.timeSlot"
            :label="apt.residentName"
          />
        </van-cell-group>
      </van-tab>
    </van-tabs>

    <van-popup v-model:show="showDatePicker" position="bottom" round>
      <van-date-picker
        v-model="currentDate"
        title="选择日期"
        @confirm="onDateConfirm"
        @cancel="showDatePicker = false"
      />
    </van-popup>

    <van-popup v-model:show="showTimePicker" position="bottom" round>
      <van-picker
        :columns="timeSlots"
        @confirm="onTimeConfirm"
        @cancel="showTimePicker = false"
      />
    </van-popup>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { showToast } from 'vant'
import PainScoreSlider from '@/components/PainScoreSlider.vue'
import dayjs from 'dayjs'

const activeTab = ref('self')
const showDatePicker = ref(false)
const showTimePicker = ref(false)
const currentDate = ref(dayjs().format('YYYY-MM-DD').split('-'))

const selfForm = ref({
  residentId: null,
  painScore: 0,
  feeling: '',
  sleepQuality: ''
})

const aptForm = ref({
  residentId: null,
  date: '',
  timeSlot: '',
  remark: ''
})

const residentOptions = ref([])
const appointments = ref([])

const timeSlots = [
  { text: '09:00-10:00', value: '09:00-10:00' },
  { text: '10:00-11:00', value: '10:00-11:00' },
  { text: '14:00-15:00', value: '14:00-15:00' },
  { text: '15:00-16:00', value: '15:00-16:00' },
  { text: '16:00-17:00', value: '16:00-17:00' }
]

function onDateConfirm({ selectedValues }) {
  aptForm.value.date = selectedValues.join('-')
  showDatePicker.value = false
}

function onTimeConfirm({ selectedValues }) {
  aptForm.value.timeSlot = selectedValues[0]
  showTimePicker.value = false
}

function onSelfAssessSubmit() {
  if (!selfForm.value.residentId) {
    showToast('请选择居民')
    return
  }
  const assessments = JSON.parse(localStorage.getItem('selfAssessments') || '[]')
  assessments.push({
    id: Date.now(),
    ...selfForm.value,
    createdAt: dayjs().format('YYYY-MM-DD HH:mm')
  })
  localStorage.setItem('selfAssessments', JSON.stringify(assessments))
  showToast({ type: 'success', message: '自评提交成功' })
  selfForm.value = { residentId: null, painScore: 0, feeling: '', sleepQuality: '' }
}

function onAppointmentSubmit() {
  if (!aptForm.value.residentId || !aptForm.value.date || !aptForm.value.timeSlot) {
    showToast('请完善预约信息')
    return
  }
  const stored = localStorage.getItem('recentResidents')
  let residentName = ''
  if (stored) {
    const residents = JSON.parse(stored)
    const found = residents.find(r => r.id === aptForm.value.residentId)
    if (found) residentName = found.name
  }
  const newAppointment = {
    id: Date.now(),
    ...aptForm.value,
    residentName
  }
  appointments.value.push(newAppointment)
  const storedAppointments = JSON.parse(localStorage.getItem('appointments') || '[]')
  storedAppointments.push(newAppointment)
  localStorage.setItem('appointments', JSON.stringify(storedAppointments))
  showToast({ type: 'success', message: '预约成功' })
  aptForm.value = { residentId: null, date: '', timeSlot: '', remark: '' }
}

onMounted(() => {
  const stored = localStorage.getItem('recentResidents')
  if (stored) {
    const residents = JSON.parse(stored)
    residentOptions.value = residents.map(r => ({
      text: `${r.name} (${r.age}岁)`,
      value: r.id
    }))
  }
  const storedAppointments = localStorage.getItem('appointments')
  if (storedAppointments) {
    appointments.value = JSON.parse(storedAppointments)
  }
})
</script>

<style scoped>
.daily-view {
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
