<template>
  <div class="resident-detail" v-if="resident">
    <van-nav-bar title="档案详情" left-arrow @click-left="$router.back()" />

    <van-cell-group inset title="基本信息" style="margin-top: 12px">
      <van-cell title="姓名" :value="resident.name" />
      <van-cell title="性别" :value="GENDER_MAP[resident.gender]" />
      <van-cell title="年龄" :value="`${resident.age}岁`" />
      <van-cell v-if="resident.phone" title="联系电话" :value="resident.phone" />
      <van-cell title="社区ID" :value="String(resident.communityId)" />
    </van-cell-group>

    <van-cell-group inset title="健康信息" style="margin-top: 12px">
      <van-cell
        v-if="resident.constitutionType"
        title="体质类型"
        :value="CONSTITUTION_MAP[resident.constitutionType]"
      />
      <van-cell
        v-if="resident.mainComplaint"
        title="主要困扰"
        :value="resident.mainComplaint"
        :label="resident.mainComplaint"
      />
      <van-cell
        v-if="resident.initialPainScore !== null && resident.initialPainScore !== undefined"
        title="初始疼痛评分"
        :value="String(resident.initialPainScore)"
      />
      <van-cell title="档案编号" :value="resident.residentId" />
      <van-cell title="创建时间" :value="formatTime(resident.createdAt)" />
    </van-cell-group>

    <div class="action-buttons">
      <van-button block type="primary" @click="$router.push(`/treatment/create?residentId=${id}`)" style="margin-bottom: 12px">
        添加调理记录
      </van-button>
      <van-button block @click="$router.push(`/trend/${id}`)">
        查看健康趋势
      </van-button>
      <van-button block plain type="primary" @click="$router.push(`/treatment/list/${id}`)" style="margin-top: 12px">
        调理历史
      </van-button>
    </div>
  </div>
  <van-loading v-else vertical class="loading">加载中...</van-loading>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getResident } from '@/api/resident'
import { GENDER_MAP, CONSTITUTION_MAP } from '@/utils/constants'
import dayjs from 'dayjs'
import { useResidentStore } from '@/stores/resident'

const route = useRoute()
const residentStore = useResidentStore()
const id = route.params.id
const resident = ref(null)

function formatTime(time) {
  return time ? dayjs(time).format('YYYY-MM-DD HH:mm') : '-'
}

onMounted(async () => {
  try {
    resident.value = await getResident(id)
    residentStore.setCurrentResident(resident.value)
  } catch (e) {
    console.error(e)
  }
})
</script>

<style scoped>
.resident-detail {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 24px;
}

.action-buttons {
  padding: 16px;
}

.loading {
  margin-top: 100px;
}
</style>
