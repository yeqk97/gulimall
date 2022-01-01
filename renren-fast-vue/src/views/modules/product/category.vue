<template>
  <div>
    <el-switch
      v-model="draggable"
      active-text="开启拖拽"
      inactive-text="关闭拖拽">
    </el-switch>
    <el-button v-if="draggable" @click="batchSave">批量保存</el-button>
    <el-button type="danger" @click="batchDelete">批量删除</el-button>
    <el-tree :data="menus" :props="defaultProps"
             :expand-on-click-node="false"
             show-checkbox
             node-key="catId"
             :default-expanded-keys="expandedKey"
             :draggable="draggable"
             :allow-drop="allowDrop"
             @node-drop="handleDrop"
             ref="menuTree">
      <span class="custom-tree-node" slot-scope="{ node, data }">
      <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="node.level <= 2"
            type="text"
            size="mini"
            @click="() => append(data)">
            Append
          </el-button>
          <el-button
            v-if="node.childNodes.length === 0"
            type="text"
            size="mini"
            @click="() => remove(node, data)">
            Delete
          </el-button>
          <el-button
            type="text"
            size="mini"
            @click="() => edit(data)">
            Edit
          </el-button>
        </span>
      </span>
    </el-tree>

    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="30%"
      :close-on-click-modal="false">
      <el-form :model="category">
        <el-form-item label="分类名称">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="category.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="计量单位">
          <el-input v-model="category.productUnit"
                    autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary"
                   @click="submitData">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // Nodes that needs to be expended after drag drop update
      pCids: [],
      draggable: false,
      // Nodes to be updated
      updatedNodes: [],
      dialogTitle: "",
      dialogType: "", //edit, add
      category: {
        name: "",
        parentCid: 0,
        catLevel: 0,
        showStatus: 1,
        sort: 0,
        catId: null,
        productUnit: "",
        icon: ""
      },
      dialogVisible: false,
      menus: [],
      expandedKey: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  methods: {
    getMenus() {
      this.$http({
        url: this.$http.adornUrl('/product/category/list/tree'),
        method: 'get'
      }).then(({data}) => {
        console.log("Data retrieved...", data.data)
        this.menus = data.data;
      })
    },

    batchDelete() {
      let checkedNodes = this.$refs.menuTree.getCheckedNodes();
      let catIds = checkedNodes.map(e => e.catId)
      this.$confirm(`是否批量删除 [${checkedNodes.map(e => e.name)}] 菜单?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/product/category/delete'),
          method: 'post',
          data: this.$http.adornData(catIds, false)
        }).then(({data}) => {
          this.$message({
            message: '菜单批量删除成功',
            type: 'success'
          });
          this.getMenus();
        });
      }).catch(() => {
      });
      console.log(catIds)
    },

    batchSave() {
      this.$http({
        url: this.$http.adornUrl('/product/category/update/sort'),
        method: 'post',
        data: this.$http.adornData(this.updatedNodes, false)
      }).then(({data}) => {
        this.$message({
          message: '菜单顺序等修改成功',
          type: 'success'
        });
        // Refresh menus
        this.getMenus();
        // Set original expanded menus
        this.expandedKey = [...this.pCids];
        this.updatedNodes = [];
        this.pCids = [];
      })
    },

    handleDrop(draggingNode, dropNode, dropType, ev) {
      console.log("handle drop: ", draggingNode, dropNode, dropType);
      // 1. Update draggingNode's parent category ID
      let pCid = dropType === "inner" ? dropNode.data.catId : dropNode.data.parentCid;
      // 2. Reorder draggingNode siblings
      let siblings = dropType === "inner" ? dropNode.childNodes : dropNode.parent.childNodes;
      for (let i = 0; i < siblings.length; i++) {
        if (siblings[i].data.catId === draggingNode.data.catId) {
          this.updatedNodes.push({
            catId: siblings[i].data.catId,
            sort: i,
            parentCid: pCid,
            catLevel: siblings[i].level
          });
          // 3. Update draggingNode's level
          this.updateChildNodeLevel(siblings[i]);
        } else {
          this.updatedNodes.push({catId: siblings[i].data.catId, sort: i});
        }
      }
      this.pCids.push(pCid);

      console.log("updated nodes:", this.updatedNodes);
    },

    updateChildNodeLevel(node) {
      for (let i = 0; i < node.childNodes.length; i++) {
        this.updatedNodes.push({
          catId: node.childNodes[i].data.catId,
          catLevel: node.childNodes[i].level
        })
        this.updateChildNodeLevel(node.childNodes[i]);
      }
    },

    allowDrop(draggingNode, dropNode, type) {
      // dragging node level
      console.log(draggingNode, dropNode, type);
      let maxLevel = this.countNodeLevel(draggingNode);
      console.log("max level:", maxLevel);
      let deep = maxLevel - draggingNode.level + 1;
      console.log("deep:", deep);

      if (type === "inner") {
        return deep + dropNode.level <= 3;
      } else {
        return deep + dropNode.parent.level <= 3;
      }
    },

    countNodeLevel(node) {
      let maxLevel = node.level;
      // Find all child levels, and return the biggest level
      if (node.childNodes != null && node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          if (node.childNodes[i].childNodes != null && node.childNodes[i].childNodes.length > 0) {
            let childMaxLevel = this.countNodeLevel(node.childNodes[i]);
            if (childMaxLevel > maxLevel) {
              maxLevel = childMaxLevel;
            }
          } else if (node.childNodes[i].level > maxLevel) {
            maxLevel = node.childNodes[i].level;
          }
        }
      }
      return maxLevel;
    },

    edit(data) {
      console.log("Data to be edited", data)
      this.$http({
        url: this.$http.adornUrl(`/product/category/info/${data.catId}`),
        method: 'get',
      }).then(({data}) => {
        this.category.name = data.data.name;
        this.category.catId = data.data.catId;
        this.category.icon = data.data.icon;
        this.category.productUnit = data.data.productUnit;
        this.category.parentCid = data.data.parentCid;
      })

      this.dialogType = "edit";
      this.dialogTitle = "修改分类"
      this.dialogVisible = true;
    },

    append(data) {
      console.log("append:", data)
      this.category.parentCid = data.catId;
      this.category.catLevel = data.catLevel * 1 + 1
      this.category.name = "";
      this.category.catId = null;
      this.category.icon = "";
      this.category.productUnit = "";

      this.dialogType = "add";
      this.dialogTitle = "添加分类";
      this.dialogVisible = true;
    },

    submitData() {
      if (this.dialogType === "add") {
        this.addCategory();
      }
      if (this.dialogType === "edit") {
        this.editCategory()
      }
    },

    editCategory() {
      let {catId, name, icon, productUnit} = this.category;
      this.$http({
        url: this.$http.adornUrl('/product/category/update'),
        method: 'post',
        data: this.$http.adornData({catId, name, icon, productUnit}, false)
      }).then(({data}) => {
        this.$message({
          message: '菜单修改成功',
          type: 'success'
        });
        // Close dialog
        this.dialogVisible = false;
        // Refresh menus
        this.getMenus();
        // Set original expanded menus
        this.expandedKey = [this.category.parentCid];
      })
    },

    addCategory() {
      console.log("Data to be added: ", this.category);
      this.$http({
        url: this.$http.adornUrl('/product/category/save'),
        method: 'post',
        data: this.$http.adornData(this.category, false)
      }).then(({data}) => {
        this.$message({
          message: '菜单保存成功',
          type: 'success'
        });
        // Close dialog
        this.dialogVisible = false;
        // Refresh menus
        this.getMenus();
        // Set original expanded menus
        this.expandedKey = [this.category.parentCid];
      })
    },

    remove(node, data) {
      let ids = [data.catId]
      this.$confirm(`是否删除 [${data.name}] 菜单?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        console.log("Remove: ", node, data)
        this.$http({
          url: this.$http.adornUrl('/product/category/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(({data}) => {
          this.$message({
            message: '菜单删除成功',
            type: 'success'
          });
          this.getMenus();
          // Set original expanded menus
          this.expandedKey = [node.parent.data.catId]
        });
      }).catch(() => {
      });
    }
  },

  created() {
    this.getMenus()
  }
}
</script>

<style scoped>

</style>
