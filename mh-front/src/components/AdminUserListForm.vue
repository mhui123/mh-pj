<template>
  <modal>
    <template #header>
      <i class="icon ion-md-close closeModalBtn" @click="showModal = false"></i>
    </template>
    <template #body>
      <div>
        <form @submit.prevent class="form">
          <label for="">기존 비밀번호</label>
          <input type="password" class="{warn-input : !warnInput}" v-model="asPw" id="asPw" @keypress="warnInput = true" />
          <p class="validation-text">
            <span class="warning" v-if="!isAsPwValid && asPw.length > 3"> 비밀번호는 8자이상, 문자와 숫자, 특수문자를 포함해주세요 </span>
          </p>
          <label for="">새 비밀번호</label>
          <input type="password" v-model="newPw" />
          <p class="validation-text">
            <span class="warning" v-if="!isNewPwValid && newPw.length > 3"> 비밀번호는 8자이상, 문자와 숫자, 특수문자를 포함해주세요 </span>
          </p>
          <label for="">새 비밀번호 확인</label>
          <input type="password" v-model="newPw2" />
          <p class="validation-text">
            <span class="warning" v-if="!isNewPw2Valid && newPw2.length > 3"> {{ pw2Warn }} </span>
          </p>
          <div>
            <button class="btn" @click="changePw">변경하기</button>
          </div>
        </form>
      </div>
    </template>
  </modal>
</template>

<script>
export default {};
</script>

<style></style>
