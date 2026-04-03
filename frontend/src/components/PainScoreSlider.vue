<template>
  <div class="pain-score-slider">
    <div class="score-display">
      <span class="score-value">{{ modelValue }}</span>
      <span class="score-label">/ 10</span>
    </div>
    <van-slider
      :model-value="modelValue"
      @update:model-value="$emit('update:modelValue', $event)"
      :min="0"
      :max="10"
      :step="0.5"
      :active-color="sliderColor"
      @change="$emit('change', $event)"
    />
    <div class="score-tips">
      <span>无痛</span>
      <span>轻度</span>
      <span>中度</span>
      <span>重度</span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  modelValue: {
    type: Number,
    default: 0
  }
})

defineEmits(['update:modelValue', 'change'])

const sliderColor = computed(() => {
  if (props.modelValue <= 3) return '#52c41a'
  if (props.modelValue <= 6) return '#faad14'
  return '#ff4d4f'
})
</script>

<style scoped>
.pain-score-slider {
  padding: 16px 0;
}

.score-display {
  text-align: center;
  margin-bottom: 12px;
}

.score-value {
  font-size: 36px;
  font-weight: bold;
  color: #333;
}

.score-label {
  font-size: 16px;
  color: #999;
}

.score-tips {
  display: flex;
  justify-content: space-between;
  margin-top: 8px;
  font-size: 12px;
  color: #999;
}
</style>
