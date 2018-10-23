<!-- HomeHeader.vue -->

<template>

<div class="home-header">
    
        <span hidefocus="true" class="menuButton">
            <a title="菜单" class="returnList" v-on:click="toggleMenu"></a>
        </span>    

        <span class="home_title">微人事</span>
        <div style="display: flex;align-items: center;margin-right: 7px">
          <el-badge style="margin-right: 30px" :is-dot="this.$store.state.nfDot">
            <i class="fa fa-bell-o" @click="goChat" style="cursor: pointer"></i>
          </el-badge>
          <el-dropdown @command="handleCommand">
  <span class="el-dropdown-link home_userinfo" style="display: flex;align-items: center">
    {{user.name}}
    <i><img v-if="user.userface!=''" :src="user.userface"
            style="width: 40px;height: 40px;margin-right: 5px;margin-left: 5px;border-radius: 40px"/></i>
  </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>个人中心</el-dropdown-item>
              <el-dropdown-item>设置</el-dropdown-item>
              <el-dropdown-item command="logout" divided>注销</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>

</div>


</template>

<style>

  .home-header {
    background-color: #20a0ff;
    color: #333;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-sizing: content-box;
    padding: 0px;
  }

  .home_title {
    color: #fff;
    font-size: 22px;
    display: inline;
    margin-left: 8px;
  }



.menuButton{
  background:transparent;
  cursor:pointer;
}

.menuButton:hover{text-decoration:none;}


.menuButton a{display:block;width:40px; height:40px;padding:0;
  margin-top:1px;
  line-height:12px;text-align:center;color:#787878;text-decoration:none;
  background:#f4f4f4 url(../assets/scrolltop.png) no-repeat 0 3px;
 
}

.menuButton a:hover{background:#759fd9 url(../assets/scrolltop.png) no-repeat 0 3px; background-position:-40px 3px !important;}

</style>


<script>  
    export default {  
        props: ['user', 'isMenuShow'],
      methods: {
        toggleMenu(){
          // var isMenuShow = this.$store.state.isMenuShow;
          isMenuShow = !isMenuShow;
          // this.$store.commit("toggleMenuShow", isMenuShow)
          alert(isMenuShow);
        }
      }
    }  
</script>  