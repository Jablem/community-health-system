<template>
  <van-cell-group inset class="treatment-item">
    <van-cell :title="`调理日期: ${record.treatmentDate}`" :value="`疼痛评分: ${record.currentPainScore ?? '-'}`">
      <template #label>
        <div class="treatment-info">
          <span>操作人: {{ record.operatorName || '-' }}</span>
          <span class="sleep-tag">睡眠: {{ SLEEP_MAP[record.sleepChange] || '-' }}</span>
        </div>
      </template>
    </van-cell>
  </van-cell-group>
</template>

<script setup>
import { SLEEP_MAP } from '@/utils/constants'

defineProps({
  record: { type: Object, required: true }
})
</script>

<style scoped>
.treatment-item {
  margin-bottom: 8px;
}

.treatment-info {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.sleep-tag {
  color: #1890ff;
}
</style>
