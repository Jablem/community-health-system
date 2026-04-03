<template>
  <div class="stats-view">
    <van-nav-bar title="健康数据统计" left-arrow @click-left="$router.back()" />

    <van-cell-group inset title="数据概览" style="margin-top: 12px">
      <van-cell title="总档案数" :value="String(totalResidents)" />
      <van-cell title="总调理记录" :value="String(totalRecords)" />
      <van-cell title="平均初始疼痛" :value="avgInitialPain.toFixed(1)" />
      <van-cell title="平均当前疼痛" :value="avgCurrentPain.toFixed(1)" />
    </van-cell-group>

    <van-cell-group inset title="疼痛改善分析" style="margin-top: 12px">
      <van-cell title="疼痛改善率" :value="`${improvementRate}%`" />
      <van-cell title="睡眠改善比例" :value="`${sleepImprovementRate}%`" />
    </van-cell-group>

    <div class="chart-section">
      <StatBarChart
        :data="communityChartData"
        title="各社区服务人数"
        height="250px"
      />
    </div>

    <van-empty v-if="totalRecords === 0" description="暂无统计数据，请先创建档案和调理记录" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import StatBarChart from '@/components/StatBarChart.vue'
import { useTreatmentStore } from '@/stores/treatment'
import { SLEEP_MAP } from '@/utils/constants'

const treatmentStore = useTreatmentStore()
const allRecords = ref([])
const allResidents = ref([])

const totalResidents = computed(() => allResidents.value.length)
const totalRecords = computed(() => allRecords.value.length)

const avgInitialPain = computed(() => {
  const scores = allResidents.value
    .map(r => r.initialPainScore)
    .filter(s => s !== null && s !== undefined)
  return scores.length ? scores.reduce((a, b) => a + b, 0) / scores.length : 0
})

const avgCurrentPain = computed(() => {
  const scores = allRecords.value
    .map(r => r.currentPainScore)
    .filter(s => s !== null && s !== undefined)
  return scores.length ? scores.reduce((a, b) => a + b, 0) / scores.length : 0
})

const improvementRate = computed(() => {
  if (allRecords.value.length === 0) return 0
  const improved = allRecords.value.filter(r => {
    if (r.currentPainScore === null || r.currentPainScore === undefined) return false
    const resident = allResidents.value.find(res => res.id === r.residentId)
    if (!resident || resident.initialPainScore === null || resident.initialPainScore === undefined) return false
    return r.currentPainScore < resident.initialPainScore
  }).length
  const valid = allRecords.value.filter(r => {
    if (r.currentPainScore === null || r.currentPainScore === undefined) return false
    const resident = allResidents.value.find(res => res.id === r.residentId)
    return resident && resident.initialPainScore !== null && resident.initialPainScore !== undefined
  }).length
  return valid > 0 ? Math.round((improved / valid) * 100) : 0
})

const sleepImprovementRate = computed(() => {
  const withSleep = allRecords.value.filter(r => r.sleepChange !== null && r.sleepChange !== undefined)
  if (withSleep.length === 0) return 0
  const improved = withSleep.filter(r => r.sleepChange <= 2).length
  return Math.round((improved / withSleep.length) * 100)
})

const communityChartData = computed(() => {
  const map = {}
  allResidents.value.forEach(r => {
    const key = `社区${r.communityId}`
    map[key] = (map[key] || 0) + 1
  })
  return Object.entries(map).map(([name, value]) => ({ name, value }))
})

onMounted(() => {
  const storedResidents = localStorage.getItem('recentResidents')
  if (storedResidents) {
    allResidents.value = JSON.parse(storedResidents)
  }
  allRecords.value = treatmentStore.records || []
})
</script>

<style scoped>
.stats-view {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 24px;
}

.chart-section {
  background: #fff;
  margin: 12px;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}
</style>
