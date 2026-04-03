<template>
  <div class="mine-view">
    <div class="user-header">
      <van-icon name="manager-o" size="60" color="#fff" />
      <h2>社区康养管理</h2>
      <p>居民健康管理服务平台</p>
    </div>

    <van-cell-group inset title="快捷功能" style="margin-top: 12px">
      <van-cell
        title="建立档案"
        icon="friends-o"
        is-link
        @click="$router.push('/resident/create')"
      />
      <van-cell
        title="调理记录"
        icon="description"
        is-link
        @click="$router.push('/treatment/create')"
      />
      <van-cell
        title="统计分析"
        icon="chart-trending-o"
        is-link
        @click="$router.push('/stats')"
      />
    </van-cell-group>

    <van-cell-group inset title="数据管理" style="margin-top: 12px">
      <van-cell
        title="我的档案"
        icon="records"
        is-link
        @click="showResidentList = true"
      />
      <van-cell
        title="调理历史"
        icon="clock-o"
        is-link
        @click="showRecordList = true"
      />
    </van-cell-group>

    <van-cell-group inset title="设置" style="margin-top: 12px">
      <van-cell title="清除缓存" is-link @click="clearCache" />
      <van-cell title="关于系统" is-link @click="showAbout = true" />
    </van-cell-group>

    <van-popup v-model:show="showResidentList" position="bottom" round style="height: 60%">
      <van-nav-bar title="我的档案" left-arrow @click-left="showResidentList = false" />
      <van-list>
        <van-cell
          v-for="r in residentList"
          :key="r.id"
          :title="r.name"
          :label="`${GENDER_MAP[r.gender]} · ${r.age}岁`"
          is-link
          @click="$router.push(`/resident/${r.id}`); showResidentList = false"
        />
        <van-empty v-if="residentList.length === 0" description="暂无档案" />
      </van-list>
    </van-popup>

    <van-popup v-model:show="showRecordList" position="bottom" round style="height: 60%">
      <van-nav-bar title="调理历史" left-arrow @click-left="showRecordList = false" />
      <van-list>
        <van-cell
          v-for="record in allRecords"
          :key="record.id"
          :title="record.treatmentDate"
          :label="`疼痛: ${record.currentPainScore ?? '-'} | 操作人: ${record.operatorName || '-'}`"
        />
        <van-empty v-if="allRecords.length === 0" description="暂无记录" />
      </van-list>
    </van-popup>

    <van-dialog v-model:show="showAbout" title="关于系统" show-cancel-button>
      <div style="padding: 16px; text-align: center">
        <p>社区康养管理系统 v1.0.0</p>
        <p style="color: #999; margin-top: 8px">居民健康档案管理 · 调理记录 · 数据统计</p>
      </div>
    </van-dialog>
  </div>

  <van-tabbar route>
    <van-tabbar-item to="/" icon="home-o">首页</van-tabbar-item>
    <van-tabbar-item to="/stats" icon="chart-trending-o">统计</van-tabbar-item>
    <van-tabbar-item to="/mine" icon="contact">我的</van-tabbar-item>
  </van-tabbar>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import { GENDER_MAP } from '@/utils/constants'
import { useTreatmentStore } from '@/stores/treatment'

const treatmentStore = useTreatmentStore()
const showResidentList = ref(false)
const showRecordList = ref(false)
const showAbout = ref(false)

const residentList = ref([])
const allRecords = ref([])

function clearCache() {
  showConfirmDialog({
    title: '清除缓存',
    message: '确定要清除本地缓存数据吗？'
  }).then(() => {
    localStorage.removeItem('recentResidents')
    localStorage.removeItem('residentCache')
    showToast('缓存已清除')
  }).catch(() => {})
}

onMounted(() => {
  const stored = localStorage.getItem('recentResidents')
  if (stored) {
    residentList.value = JSON.parse(stored)
  }
  allRecords.value = treatmentStore.records || []
})
</script>

<style scoped>
.mine-view {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 60px;
}

.user-header {
  background: linear-gradient(135deg, #1890ff, #36cfc9);
  color: #fff;
  padding: 40px 16px 32px;
  text-align: center;
}

.user-header h2 {
  font-size: 20px;
  margin-top: 12px;
}

.user-header p {
  font-size: 13px;
  opacity: 0.85;
  margin-top: 4px;
}
</style>
