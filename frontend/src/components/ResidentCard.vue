<template>
  <van-card
    :title="resident.name"
    :desc="`${GENDER_MAP[resident.gender]} · ${resident.age}岁`"
    class="resident-card"
  >
    <template #tags>
      <van-tag type="primary" size="medium" style="margin-right: 4px">
        {{ CONSTITUTION_MAP[resident.constitutionType] || '未知体质' }}
      </van-tag>
      <van-tag type="warning" size="medium">
        疼痛评分: {{ resident.initialPainScore ?? '未评估' }}
      </van-tag>
    </template>
    <template #footer>
      <van-button size="mini" @click="$emit('view-trend', resident.id)">趋势</van-button>
      <van-button size="mini" type="primary" @click="$emit('add-record', resident.id)" style="margin-left: 8px">
        调理记录
      </van-button>
    </template>
  </van-card>
</template>

<script setup>
import { GENDER_MAP, CONSTITUTION_MAP } from '@/utils/constants'

defineProps({
  resident: { type: Object, required: true }
})

defineEmits(['view-trend', 'add-record'])
</script>

<style scoped>
.resident-card {
  margin-bottom: 12px;
  border-radius: 8px;
  overflow: hidden;
}
</style>
