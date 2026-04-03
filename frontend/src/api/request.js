import axios from 'axios'
import { showToast } from 'vant'

const request = axios.create({
  baseURL: '/api/v1',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code !== 200) {
      showToast({
        message: res.message || '请求失败',
        type: 'fail'
      })
      return Promise.reject(new Error(res.message))
    }
    return res.data
  },
  (error) => {
    const message = error.response?.data?.message || error.message || '网络异常'
    showToast({ message, type: 'fail' })
    return Promise.reject(error)
  }
)

export default request
