<template>
  <div class="main list-container contents">
    <h1 class="page-header">용어사전</h1>
    <ul>
      <li>
        <i class="icon ion-md-alert nothing-icon"></i>
        <p class="zero-result">페이지를 찾을 수 없습니다.</p>
      </li>
    </ul>
  </div>
</template>

<script>
export default {};
</script>

<style scoped>
.search-icon {
  font-size: 2rem;
}
.nothing-icon {
  font-size: 4rem;
  text-align: center;
  display: block;
}
.zero-result {
  /* text-align: center;
  width: 100%;
  height: 40%; */
  margin: 3rem auto;
  font-size: large;
}
.zero-result .link {
  text-align: center;
  color: lightblue;
  display: flow;
  width: 100%;
  font-size: larger;
}
</style>
