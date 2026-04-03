<template>
  <div class="report-view">
    <van-nav-bar title="阶段评估报告" left-arrow @click-left="$router.back()" />

    <van-empty v-if="!resident" description="请先选择居民" />

    <template v-if="resident">
      <van-cell-group inset title="居民信息" style="margin-top: 12px">
        <van-cell title="姓名" :value="resident.name" />
        <van-cell title="年龄/性别" :value="`${resident.age}岁 / ${GENDER_MAP[resident.gender]}`" />
        <van-cell
          v-if="resident.constitutionType"
          title="体质类型"
          :value="CONSTITUTION_MAP[resident.constitutionType]"
        />
      </van-cell-group>

      <van-cell-group inset title="评估摘要" style="margin-top: 12px">
        <van-cell title="初始疼痛评分" :value="String(resident.initialPainScore ?? '-')" />
        <van-cell title="当前疼痛评分" :value="currentPain" />
        <van-cell title="疼痛改善" :value="painImprovement" />
        <van-cell title="调理次数" :value="String(records.length)" />
        <van-cell title="睡眠改善情况" :value="sleepSummary" />
      </van-cell-group>

      <div class="chart-container">
        <TrendLineChart :data="records" title="疼痛评分变化趋势" />
      </div>

      <van-cell-group inset title="调理记录明细" style="margin-top: 12px">
        <van-cell
          v-for="r in records"
          :key="r.id"
          :title="r.treatmentDate"
          :value="`疼痛: ${r.currentPainScore ?? '-'}`"
          :label="`操作人: ${r.operatorName || '-'} | 睡眠: ${SLEEP_MAP[r.sleepChange] || '-'}`"
        />
      </van-cell-group>

      <div class="export-btn">
        <van-button block type="primary" icon="share-o" @click="exportReport">
          导出报告
        </van-button>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getResident } from '@/api/resident'
import { getRecordsByResident } from '@/api/treatment'
import TrendLineChart from '@/components/TrendLineChart.vue'
import { GENDER_MAP, CONSTITUTION_MAP, SLEEP_MAP } from '@/utils/constants'
import { showToast } from 'vant'

const route = useRoute()
const id = route.params.id
const resident = ref(null)
const records = ref([])

const currentPain = computed(() => {
  const valid = records.value.filter(r => r.currentPainScore !== null && r.currentPainScore !== undefined)
  return valid.length ? String(valid[valid.length - 1].currentPainScore) : '-'
})

const painImprovement = computed(() => {
  const initial = resident.value?.initialPainScore
  if (!initial || records.value.length === 0) return '-'
  const current = records.value
    .filter(r => r.currentPainScore !== null && r.currentPainScore !== undefined)
    .pop()?.currentPainScore
  if (!current) return '-'
  const diff = (initial - current).toFixed(1)
  return diff > 0 ? `↓ 改善 ${diff}` : diff < 0 ? `↑ 上升 ${Math.abs(diff)}` : '无变化'
})

const sleepSummary = computed(() => {
  const withSleep = records.value.filter(r => r.sleepChange !== null && r.sleepChange !== undefined)
  if (withSleep.length === 0) return '无记录'
  const improved = withSleep.filter(r => r.sleepChange <= 2).length
  return `${improved}/${withSleep.length} 次改善`
})

function exportReport() {
  showToast('报告导出功能开发中')
}

onMounted(async () => {
  try {
    resident.value = await getResident(id)
    records.value = await getRecordsByResident(id)
  } catch (e) {
    console.error(e)
  }
})
</script>

<style scoped>
.report-view {
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

.export-btn {
  padding: 24px 16px 0;
}
</style>
