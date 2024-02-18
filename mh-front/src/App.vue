<template>
<div>
  <img alt="Vue logo" src="./assets/logo.png">
  <HelloWorld msg="Welcome to Your Vue.js App"/>
  <button @click="apitest">test</button>
</div>
  
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import axios from 'axios';

export default {
  name: 'App',
  components: {
    HelloWorld
  },
  methods:{
    apitest(){
      let user = new Object();
      user['id'] = 'admin';
      user['pw'] = 'admin01!'
      // let loginUrl = '/user/login';
      let signinUrl = 'api/test';
      axios.post(signinUrl, user).then(function(res){
        console.log(`success translate : ${res.data}`);
      }).catch(function(err){
        console.log(err);
      })
    },
    // reqApi : async function(data = []){
    //   let _rootpath = location.origin;
    //   let type = 'POST';
    //   let url = `${_rootpath}/api/test`
    // }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
