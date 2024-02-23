const { defineConfig } = require('@vue/cli-service');
module.exports = defineConfig({
  transpileDependencies: true,
  //for proxy
  outputDir: '../mhProject_back/src/main/resources/static',
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:9900',
        changeOrigin: true,
        // pathRewrite: {'^/user': ''}
      },
      '/user': {
        target: 'http://localhost:9900',
        changeOrigin: true,
      },
    },
  },
  // css: {
  //   loaderOptions: {
  //     sass: {
  //       additionalData: `@import "@/styles/sidebar.scss";`,
  //     },
  //   },
  // },
});
