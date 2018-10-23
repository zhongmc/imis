<!-- HomeHeader.vue -->

<template>

<div class="nav-content showNav" v-v-bind:class="[showMenuClass]" v-on:mouseleave="toggleMenu" v-show="isMenuShow">
            <el-menu style="background: #ececec;width: 180px;" unique-opened router>
              <template v-for="(item,index) in routes" v-if="!item.hidden">
                <el-submenu :key="index" :index="index+''">
                  <template slot="title">
                    <i :class="item.iconCls" style="color: #20a0ff;width: 14px;"></i>
                    <span slot="title">{{item.name}}</span>
                  </template>
                  <el-menu-item width="180px"
                                style="padding-left: 30px;padding-right:0px;margin-left: 0px;width: 170px;text-align: left"
                                v-for="child in item.children"
                                :index="child.path"
                                :key="child.path">{{child.name}}
                  </el-menu-item>
                </el-submenu>
              </template>
            </el-menu>
</div>


</template>

<style>

.nav-content { position: fixed; top: 50px;  left: -100%; width:250px; background: #393838; display: block; height: 100%; z-index: 9; }




 @keyframes showNav {  from {
 left: -100%;
}
to { left: 20px; }
}
 @-webkit-keyframes showNav {  from {
 left: -100%;
}
to { left: 20px; }
}
 @-moz-keyframes showNav {  from {
  left: -100%;
}
to { left: 20px; }
}
 @-o-keyframes showNav {  from {
  left: -100%;
}
to { left: 20px; }
}

.showNav { -webkit-animation: showNav 0.5s ease forwards; -moz-animation: showNav 0.5s ease forwards; -o-animation: showNav 0.5s ease forwards; 
      animation: showNav 0.5s ease forwards; }


@keyframes hideNav {  from {
  left: 20px;
}
to { left: -100%; }
}
 @-webkit-keyframes hideNav {  from {
  left: 20px;
}
to { left: -100%; }
}
 @-moz-keyframes hideNav {  from {
  left: 20px;
}
to { left: -100%; }
}
 @-o-keyframes hideNav {  from {
  left: 20px;
}
to { left: -100%; }
}
.hideNav { -webkit-animation: hideNav 0.5s ease forwards; -moz-animation: hideNav 0.5s ease forwards; -o-animation: hideNav 0.5s ease forwards; animation: hideNav 0.5s ease forwards; }
.hidden { display: none; }


</style>


<script>  
    export default { 
      methods:{
     toggleMenu(){
        // var isMenuShow = this.$store.state.isMenuShow;
        isMenuShow = !isMenuShow;
        if(isMenuShow)
          showMenuClass = 'hideNav';
        else
          showMenuClass = 'showNav';

        // this.$store.commit("toggleMenuShow", isMenuShow)

      }
      },
      props: ['routes', 'isMenuShow', 'showMenuClass']
    }  
</script>  