const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  //for proxy
  outputDir: "../mhProject/src/main/resources/static",
  devServer:{
    proxy:{
      '/api':{
        target: 'http://localhost:9900',
        changeOrigin: true,
        // pathRewrite: {'^/user': ''}
      }
    }
  }
})
