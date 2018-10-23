<template>
  <div style="margin-top: 10px">
      <el-tabs  type="card" v-model="activeTab">
        <el-tab-pane label="客户维护" name="customMana">
          <custom-mana></custom-mana>
        </el-tab-pane>
        <el-tab-pane label="客户联系人" name="customContractor">
          <custom-contractor></custom-contractor>
        </el-tab-pane>
      </el-tabs>
  </div>
</template>



<script>

  import CustomMana from './CustomMana.vue'
  import CustomContractor from './CustomContractor.vue'


  export default{
    mounted: function () {
      // this.initData();
    },

    methods: {
      initData(){
        var _this = this;
        this.loading = true;
       this.getRequest("/system/budget/settings").then(resp=> {
          _this.loading = false;
          if (resp && resp.status == 200) {
            _this.budgetTypes = resp.data.budgetTypes;
          }
        })
      }
    },

    components: {
      'custom-mana': CustomMana,
      'custom-contractor': CustomContractor
    },

    data(){
      return {
        loading: false,
        activeTab:"customMana"
      }
    }
  }
</script>
