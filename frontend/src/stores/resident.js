import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useResidentStore = defineStore('resident', () => {
  const currentResident = ref(null)

  function setCurrentResident(resident) {
    currentResident.value = resident
  }

  function clearCurrentResident() {
    currentResident.value = null
  }

  return { currentResident, setCurrentResident, clearCurrentResident }
})
