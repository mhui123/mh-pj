var path = require('path')
var webpack = require('webpack')

module.exports = {
  mode: 'development',
  entry: './src/main.js',
  output: {
    path: path.resolve(__dirname, './dist'),
    publicPath: '/dist/',
    filename: 'build.js'
  },
  module: {
    rules: [
      {
        test: /\.css$/,
        use: [
          'vue-style-loader',
          'css-loader'
        ],
      },      
      {
        test: /\.vue$/,
        loader: 'vue-loader',
        options: {
          loaders: {
          }
          // other vue-loader options go here
        }
      },
      {
        test: /\.js$/,
        loader: 'babel-loader',
        exclude: /node_modules/
      },
      {
        test: /\.(png|jpg|gif|svg)$/,
        loader: 'file-loader',
        options: {
          name: '[name].[ext]?[hash]'
        }
      }
    ]
  },
  resolve: {
    // 웹팩으로 파일 연관관계를 해석하는 방식의 정의. vue$을 ~/vue.esm.js로 인식하겠다.
    alias: {
      'vue$': 'vue/dist/vue.esm.js'
    },
    // 해당 확장자는 붙이지 않아도 인식하겠다.
    extensions: ['.*', '.js', '.vue', '.json']
  },
  devServer: {
    static: {
      directory: path.resolve(__dirname, 'dist'), // 개발 서버가 제공할 파일들의 경로
    },
    port: 9901, // 개발 서버의 포트 번호
    hot: true // 변경 사항이 발생하면 페이지를 새로고침하지 않고 변경 사항을 즉시 반영
  },
  performance: {
    hints: false
  },
  devtool: 'source-map'
}