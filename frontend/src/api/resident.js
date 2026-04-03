import request from './request'

export function createResident(data) {
  return request.post('/residents', data)
}

export function getResident(id) {
  return request.get(`/residents/${id}`)
}

export function getAllResidents() {
  return request.get('/residents')
}
