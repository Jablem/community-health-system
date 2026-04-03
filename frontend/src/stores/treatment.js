import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useTreatmentStore = defineStore('treatment', () => {
  const records = ref([])
  const loading = ref(false)

  function setRecords(list) {
    records.value = list
  }

  function addRecord(record) {
    records.value.push(record)
  }

  function clearRecords() {
    records.value = []
  }

  return { records, loading, setRecords, addRecord, clearRecords }
})
