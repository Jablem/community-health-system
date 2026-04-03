<template>
  <div class="home-view">
    <div class="header">
      <h1>社区康养管理系统</h1>
      <p class="subtitle">居民健康管理服务平台</p>
    </div>

    <div class="quick-stats">
      <StatCard :value="todayCount" label="今日服务" color="#e6f7ff" />
      <StatCard :value="monthCount" label="本月累计" color="#f6ffed" />
    </div>

    <div class="grid-menu">
      <van-grid :column-num="3" :border="false">
        <van-grid-item icon="friends-o" text="建立档案" @click="$router.push('/resident/create')" />
        <van-grid-item icon="description" text="调理记录" @click="$router.push('/treatment/create')" />
        <van-grid-item icon="chart-trending-o" text="统计分析" @click="$router.push('/stats')" />
        <van-grid-item icon="records" text="阶段报告" @click="goToReport" />
        <van-grid-item icon="calendar-o" text="预约自评" @click="$router.push('/daily')" />
        <van-grid-item icon="contact" text="我的" @click="$router.push('/mine')" />
      </van-grid>
    </div>

    <div class="recent-section">
      <h3>最近档案</h3>
      <van-empty v-if="recentResidents.length === 0" description="暂无档案数据" />
      <ResidentCard
        v-for="r in recentResidents"
        :key="r.id"
        :resident="r"
        @click="$router.push(`/resident/${r.id}`)"
      />
    </div>
  </div>

  <van-tabbar route>
    <van-tabbar-item to="/" icon="home-o">首页</van-tabbar-item>
    <van-tabbar-item to="/stats" icon="chart-trending-o">统计</van-tabbar-item>
    <van-tabbar-item to="/mine" icon="contact">我的</van-tabbar-item>
  </van-tabbar>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import StatCard from '@/components/StatCard.vue'
import ResidentCard from '@/components/ResidentCard.vue'
import dayjs from 'dayjs'

const router = useRouter()
const todayCount = ref(0)
const monthCount = ref(0)
const recentResidents = ref([])

function goToReport() {
  if (recentResidents.value.length === 0) {
    return
  }
  if (recentResidents.value.length === 1) {
    router.push(`/report/${recentResidents.value[0].id}`)
    return
  }
  router.push('/stats')
}

onMounted(async () => {
  const stored = localStorage.getItem('recentResidents')
  if (stored) {
    recentResidents.value = JSON.parse(stored)
  }
  const appointments = JSON.parse(localStorage.getItem('appointments') || '[]')
  const today = dayjs().format('YYYY-MM-DD')
  const monthStart = dayjs().startOf('month').format('YYYY-MM-DD')
  todayCount.value = appointments.filter(a => a.date === today).length
  monthCount.value = appointments.filter(a => a.date >= monthStart).length
})
</script>

<style scoped>
.home-view {
  padding-bottom: 60px;
  background: #f5f5f5;
  min-height: 100vh;
}

.header {
  background: linear-gradient(135deg, #1890ff, #36cfc9);
  color: #fff;
  padding: 32px 16px 24px;
  text-align: center;
}

.header h1 {
  font-size: 22px;
  font-weight: 600;
}

.subtitle {
  font-size: 13px;
  opacity: 0.85;
  margin-top: 4px;
}

.quick-stats {
  display: flex;
  gap: 12px;
  padding: 16px;
  margin-top: -16px;
}

.quick-stats > * {
  flex: 1;
}

.grid-menu {
  background: #fff;
  margin: 0 12px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.recent-section {
  padding: 16px;
}

.recent-section h3 {
  font-size: 16px;
  margin-bottom: 12px;
  color: #333;
}
</style>
