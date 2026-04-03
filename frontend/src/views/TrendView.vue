<template>
  <div class="trend-view">
    <van-nav-bar title="健康趋势" left-arrow @click-left="$router.back()" />

    <div class="chart-container" v-if="records.length > 0">
      <TrendLineChart :data="records" title="疼痛评分趋势" />
    </div>

    <van-cell-group inset title="趋势分析" style="margin-top: 12px" v-if="records.length >= 2">
      <van-cell title="首次评分" :value="String(firstScore)" />
      <van-cell title="最近评分" :value="String(lastScore)" />
      <van-cell title="变化幅度" :value="`${changePercent}%`" />
      <van-cell title="调理次数" :value="String(records.length)" />
    </van-cell-group>

    <van-empty v-if="records.length === 0" description="暂无趋势数据" />

    <div class="records-section" v-if="records.length > 0">
      <h3>详细记录</h3>
      <TreatmentItem v-for="record in records" :key="record.id" :record="record" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getRecordsByResident } from '@/api/treatment'
import TrendLineChart from '@/components/TrendLineChart.vue'
import TreatmentItem from '@/components/TreatmentItem.vue'

const props = defineProps({ id: { type: [String, Number], required: true } })

const records = ref([])

const firstScore = computed(() => {
  const valid = records.value.filter(r => r.currentPainScore !== null && r.currentPainScore !== undefined)
  return valid.length ? valid[0].currentPainScore : '-'
})

const lastScore = computed(() => {
  const valid = records.value.filter(r => r.currentPainScore !== null && r.currentPainScore !== undefined)
  return valid.length ? valid[valid.length - 1].currentPainScore : '-'
})

const changePercent = computed(() => {
  const valid = records.value.filter(r => r.currentPainScore !== null && r.currentPainScore !== undefined)
  if (valid.length < 2) return '-'
  const first = valid[0].currentPainScore
  const last = valid[valid.length - 1].currentPainScore
  if (first === 0) return 0
  return Math.round(((first - last) / first) * 100)
})

onMounted(async () => {
  try {
    records.value = await getRecordsByResident(props.id)
  } catch (e) {
    console.error(e)
  }
})
</script>

<style scoped>
.trend-view {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 24px;
}

.chart-container {
  background: #fff;
  margin: 12px;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.records-section {
  padding: 16px;
}

.records-section h3 {
  font-size: 16px;
  margin-bottom: 12px;
}
</style>
