import request from './request'

export function createRecord(data) {
  return request.post('/records', data)
}

export function getRecordsByResident(residentId) {
  return request.get(`/residents/${residentId}/records`)
}
