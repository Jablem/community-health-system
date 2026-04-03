<template>
  <div class="resident-create">
    <van-nav-bar title="建立居民健康档案" left-arrow @click-left="$router.back()" />

    <van-form @submit="onSubmit" class="form-container">
      <van-cell-group inset title="基本信息">
        <van-field
          v-model="form.name"
          name="name"
          label="姓名"
          placeholder="请输入姓名"
          :rules="[{ required: true, message: '请输入姓名' }]"
        />
        <van-field name="gender" label="性别">
          <template #input>
            <van-radio-group v-model="form.gender" direction="horizontal">
              <van-radio :name="1">男</van-radio>
              <van-radio :name="2">女</van-radio>
            </van-radio-group>
          </template>
          <template #extra>
            <span v-if="!form.gender" style="color: #ee0a24; font-size: 12px">必选</span>
          </template>
        </van-field>
        <van-field
          v-model.number="form.age"
          type="digit"
          name="age"
          label="年龄"
          placeholder="请输入年龄"
          :rules="[{ required: true, message: '请输入年龄' }]"
        />
        <van-field
          v-model="form.phone"
          type="tel"
          name="phone"
          label="联系电话"
          placeholder="请输入联系电话"
        />
      </van-cell-group>

      <van-cell-group inset title="健康信息" style="margin-top: 12px">
        <van-field
          v-model.number="form.communityId"
          type="number"
          name="communityId"
          label="社区ID"
          placeholder="请输入社区ID"
          :rules="[{ required: true, message: '请输入社区ID' }]"
        />
        <van-field
          v-model="constitutionDisplay"
          name="constitutionType"
          label="体质类型"
          placeholder="请选择体质类型"
          readonly
          is-link
          @click="showConstitutionPicker = true"
        />
        <van-field
          v-model="form.mainComplaint"
          name="mainComplaint"
          label="主要困扰"
          type="textarea"
          rows="3"
          placeholder="请描述主要健康问题或困扰"
          autosize
        />
        <van-cell title="初始疼痛评分" class="pain-cell">
          <template #value>
            <PainScoreSlider v-model="form.initialPainScore" />
          </template>
        </van-cell>
      </van-cell-group>

      <div class="submit-btn">
        <van-button round block type="primary" native-type="submit" :loading="submitting">
          提交档案
        </van-button>
      </div>
    </van-form>

    <van-popup v-model:show="showConstitutionPicker" position="bottom" round>
      <van-picker
        :columns="constitutionColumns"
        @confirm="onConstitutionConfirm"
        @cancel="showConstitutionPicker = false"
      />
    </van-popup>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { createResident } from '@/api/resident'
import PainScoreSlider from '@/components/PainScoreSlider.vue'
import { CONSTITUTION_MAP } from '@/utils/constants'
import { useResidentStore } from '@/stores/resident'

const router = useRouter()
const residentStore = useResidentStore()
const submitting = ref(false)

const form = ref({
  name: '',
  gender: null,
  age: null,
  phone: '',
  communityId: null,
  constitutionType: null,
  mainComplaint: '',
  initialPainScore: 0
})

const constitutionColumns = Object.entries(CONSTITUTION_MAP).map(([value, text]) => ({
  text,
  value: Number(value)
}))

const constitutionDisplay = ref('')
const showConstitutionPicker = ref(false)

function onConstitutionConfirm({ selectedOptions }) {
  const item = selectedOptions[0]
  form.value.constitutionType = item.value
  constitutionDisplay.value = item.text
  showConstitutionPicker.value = false
}

async function onSubmit() {
  if (!form.value.gender) {
    showToast('请选择性别')
    return
  }
  submitting.value = true
  try {
    const data = { ...form.value }
    if (!data.constitutionType) delete data.constitutionType
    if (!data.mainComplaint) delete data.mainComplaint
    if (!data.phone) delete data.phone
    const result = await createResident(data)
    residentStore.setCurrentResident(result)
    showToast({ type: 'success', message: '档案创建成功' })
    router.push(`/resident/${result.id}`)
  } catch (e) {
    console.error(e)
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.resident-create {
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
