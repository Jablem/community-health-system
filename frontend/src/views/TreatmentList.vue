<template>
  <div class="treatment-list">
    <van-nav-bar :title="`调理历史 (${records.length}条)`" left-arrow @click-left="$router.back()" />

    <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
      <van-list v-model:loading="loading" :finished="finished" finished-text="没有更多了">
        <TreatmentItem
          v-for="record in records"
          :key="record.id"
          :record="record"
        />
        <van-empty v-if="!loading && records.length === 0" description="暂无调理记录" />
      </van-list>
    </van-pull-refresh>

    <div class="fab-btn" @click="$router.push(`/treatment/create?residentId=${id}`)">
      <van-button type="primary" round size="large" icon="plus">
        新增记录
      </van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getRecordsByResident } from '@/api/treatment'
import TreatmentItem from '@/components/TreatmentItem.vue'
import { useTreatmentStore } from '@/stores/treatment'

const props = defineProps({ id: { type: [String, Number], required: true } })

const treatmentStore = useTreatmentStore()
const refreshing = ref(false)
const loading = ref(false)
const finished = ref(false)

const records = ref([])

async function loadRecords() {
  try {
    const data = await getRecordsByResident(props.id)
    records.value = data
    treatmentStore.setRecords(data)
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
    refreshing.value = false
    finished.value = true
  }
}

function onRefresh() {
  finished.value = false
  loading.value = true
  loadRecords()
}

onMounted(loadRecords)
</script>

<style scoped>
.treatment-list {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 80px;
}

.fab-btn {
  position: fixed;
  bottom: 24px;
  left: 16px;
  right: 16px;
  z-index: 10;
}
</style>
