<template>
    <div>
        <h4 class="lighter">
            <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
            <router-link to="/business/course" class="pink"> {{course.name}} </router-link>：
            <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
            <router-link to="/business/chapter" class="pink"> {{chapter.name}} </router-link>
        </h4>
        <hr>
        <p>
            <button v-on:click="add()" class="btn btn-white btn-default btn-round">
                <i class="ace-icon fa fa-edit"></i>
                新增
            </button>
            &nbsp;
            <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
                <i class="ace-icon fa fa-refresh"></i>
                刷新
            </button>
        </p>

        <pagination ref="pagination" v-bind:list="list" v-bind:itemCount="8"></pagination>

        <table id="simple-table" class="table  table-bordered table-hover">
            <thead>
            <tr>
                        <th>ID</th>
                        <th>标题</th>
                        <th>视频</th>
                        <th>时长</th>
                        <th>收费</th>
                        <th>顺序</th>
                        <th>操作</th>
            </tr>
            </thead>

            <tbody>
            <tr v-for="section in sections">
                            <td>{{section.id}}</td>
                            <td>{{section.title}}</td>
                            <td>{{section.video}}</td>
                            <td>{{section.time | formatSecond}}</td>
                            <td>{{SECTION_CHARGE | optionKV(section.charge)}}</td>
                            <td>{{section.sort}}</td>
        <td>
            <div class="hidden-sm hidden-xs btn-group">
                <button v-on:click="edit(section)" class="btn btn-xs btn-info">
                    <i class="ace-icon fa fa-pencil bigger-120"></i>
                </button>
                <button v-on:click="del(section.id)" class="btn btn-xs btn-danger">
                    <i class="ace-icon fa fa-trash-o bigger-120"></i>
                </button>
            </div>
        </td>
        </tr>
        </tbody>
        </table>

        <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">表单</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">标题</label>
                                        <div class="col-sm-10">
                                            <input v-model="section.title" class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">课程</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">{{course.name}}</p>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">大章</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">{{chapter.name}}</p>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">视频</label>
                                        <div class="col-sm-10">
                                            <input v-model="section.video" class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">时长</label>
                                        <div class="col-sm-10">
                                            <input v-model="section.time" class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">收费</label>
                                        <div class="col-sm-10">
                                            <select v-model="section.charge" class="form-control">
                                                <option v-for="o in SECTION_CHARGE" v-bind:value="o.key">{{o.value}}</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">顺序</label>
                                        <div class="col-sm-10">
                                            <input v-model="section.sort" class="form-control">
                                        </div>
                                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button v-on:click="save()" type="button" class="btn btn-primary">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    </div>
</template>

<script>
    import Pagination from "../../components/pagination";
    export default {
        components: {Pagination},
        name: "business-section",
        data :function(){
            return {
                section: {},
                sections : [],
                SECTION_CHARGE:SECTION_CHARGE,
                course: {},
                chapter: {},
            }
        },
        mounted: function () {
            // this.$parent.activeSidebar("business-section-sidebar");
            let _this=this;
            _this.$refs.pagination.size=5;
            let course = SessionStorage.get(SESSION_KEY_COURSE) || {};
            let chapter = SessionStorage.get(SESSION_KEY_CHAPTER) || {};
            if (Tool.isEmpty(course) || Tool.isEmpty(chapter)) {
                _this.$router.push("/welcome");
            }
            _this.course = course;
            _this.chapter = chapter;
            _this.list(1);
            // sidebar激活样式方法一
            this.$parent.activeSidebar("business-course-sidebar");
        } ,
        methods:{
            /**
             * 点击【新增】
             */
            add() {
                let _this = this;
                _this.section = {};
                $("#form-modal").modal("show");
            },
            edit(section) {
                let _this = this;
                _this.section = $.extend({},section);
                $("#form-modal").modal("show");
            },
            list(page){
                let _this = this;
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/section/list',{
                    page:page,
                    size:_this.$refs.pagination.size,
                    courseId: _this.course.id,
                    chapterId: _this.chapter.id,
                }).then((response) => {
                    console.log("查询小节列表结果：", response);
                    let resp=response.data;
                    _this.sections=resp.content.list;
                    _this.$refs.pagination.render(page,resp.content.total);
                })
            },
            save(page){
                let _this = this;

                // 保存校验
                if (1 != 1
                    || !Validator.require(_this.section.title, "标题")
                    || !Validator.length(_this.section.video, "视频", 1, 200)
                ) {
                    return;
                }
                _this.section.courseId = _this.course.id;
                _this.section.chapterId = _this.chapter.id;

                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/section/save',
                    _this.section).then((response) => {
                    console.log("保存小节列表结果：", response);
                    let resp=response.data;
                    if (resp.success){
                        $("#form-modal").modal("hide");
                        _this.list(1);
                        Toast.success("保存成功")
                    }else {
                        Toast.warning(resp.message)
                    }
                })
            },
            del(id){
                let _this = this;
                Confirm.show("删除小节后不可恢复，是否删除？",function () {
                    _this.$ajax.delete(process.env.VUE_APP_SERVER + '/business/admin/section/delete/' + id).then((response) => {
                        console.log("删除小节列表结果：", response);
                        let resp=response.data;
                        if (resp.success){
                            _this.list(1);
                            Toast.success("删除成功!")
                        }
                    })
                });
            }
        }
    }
</script>

