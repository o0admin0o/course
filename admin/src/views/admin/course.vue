<template>
    <div>
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

        <div class="row">
            <div v-for="course in courses" class="col-md-4">
                <div class="thumbnail search-thumbnail">
                    <img v-show="!course.image" class="media-object" src="/static/image/demo-course.jpg" />
                    <img v-show="course.image" class="media-object" v-bind:src="course.image" />
                    <div class="caption">
                        <div class="clearfix">
              <span class="pull-right label label-primary info-label">
                {{COURSE_LEVEL | optionKV(course.level)}}
              </span>
                            <span class="pull-right label label-primary info-label">
                {{COURSE_CHARGE | optionKV(course.charge)}}
              </span>
                            <span class="pull-right label label-primary info-label">
                {{COURSE_STATUS | optionKV(course.status)}}
              </span>
                        </div>

                        <h3 class="search-title">
                            <a href="#" class="blue">{{course.name}}</a>
                        </h3>

                        <div v-for="teacher in teachers.filter(t=>{return t.id===course.teacherId})" class="profile-activity clearfix">
                            <div>
                                <img v-show="!teacher.image" class="pull-left" src="/static/image/讲师头像/头像1.jpg">
                                <img v-show="teacher.image" class="pull-left" v-bind:src="teacher.image">
                                <a class="user" href="#"> {{teacher.name}} </a>
                                <br>
                                {{teacher.position}}
                            </div>
                        </div>

                        <p>
                            <span class="blue bolder bigger-150">{{course.price}}&nbsp;<i class="fa fa-rmb"></i></span>&nbsp;
                        </p>
                        <p>{{course.summary}}</p>
                        <p>
                            <span class="badge badge-info">{{course.id}}</span>
                            <span class="badge badge-info">排序：{{course.sort}}</span>
                            <span class="badge badge-info">{{course.time | formatSecond}}</span>
                        </p>
                        <p>
                            <button v-on:click="toChapter(course)" class="btn btn-white btn-xs btn-info btn-round">
                                大章
                            </button>&nbsp;
                            <button v-on:click="editContent(course)" class="btn btn-white btn-xs btn-info btn-round">
                                内容
                            </button>&nbsp;
                            <button v-on:click="openSortModal(course)" class="btn btn-white btn-xs btn-info btn-round">
                                排序
                            </button>&nbsp;
                            <button v-on:click="edit(course)" class="btn btn-white btn-xs btn-info btn-round">
                                编辑
                            </button>&nbsp;
                            <button v-on:click="del(course.id)" class="btn btn-white btn-xs btn-info btn-round">
                                删除
                            </button>&nbsp;
                        </p>
                    </div>
                </div>
            </div>
        </div>


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
                                <label class="col-sm-2 control-label">
                                    分类
                                </label>
                                <div class="col-sm-10">
                                    <ul id="tree" class="ztree"></ul>
                                </div>
                            </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">名称</label>
                                        <div class="col-sm-10">
                                            <input v-model="course.name" class="form-control">
                                        </div>
                                    </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">讲师</label>
                                <div class="col-sm-10">
                                    <select v-model="course.teacherId" class="form-control">
                                        <option v-for="o in teachers" v-bind:value="o.id">{{o.name}}</option>
                                    </select>
                                </div>
                            </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">概述</label>
                                        <div class="col-sm-10">
                                            <input v-model="course.summary" class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">时长</label>
                                        <div class="col-sm-10">
                                            <input v-model="course.time" class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">价格（元）</label>
                                        <div class="col-sm-10">
                                            <input v-model="course.price" class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">封面</label>
                                        <div class="col-sm-10">
                                            <input v-model="course.image" class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">级别</label>
                                        <div class="col-sm-10">
                                            <select v-model="course.level" class="form-control">
                                                <option v-for="o in COURSE_LEVEL" v-bind:value="o.key">{{o.value}}</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">收费</label>
                                        <div class="col-sm-10">
                                            <select v-model="course.charge" class="form-control">
                                                <option v-for="o in COURSE_CHARGE" v-bind:value="o.key">{{o.value}}</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">状态</label>
                                        <div class="col-sm-10">
                                            <select v-model="course.status" class="form-control">
                                                <option v-for="o in COURSE_STATUS" v-bind:value="o.key">{{o.value}}</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">报名数</label>
                                        <div class="col-sm-10">
                                            <input v-model="course.enroll" class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">顺序</label>
                                        <div class="col-sm-10">
                                            <input v-model="course.sort" class="form-control" disabled>
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

        <div id="course-sort-modal" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">排序</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="control-label col-lg-3">
                                    当前排序
                                </label>
                                <div class="col-lg-9">
                                    <input class="form-control" v-model="sort.oldSort" name="oldSort" disabled>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-lg-3">
                                    新排序
                                </label>
                                <div class="col-lg-9">
                                    <input class="form-control" v-model="sort.newSort" name="newSort">
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">
                            <i class="ace-icon fa fa-times"></i>
                            取消
                        </button>
                        <button type="button" class="btn btn-white btn-info btn-round" v-on:click="updateSort()">
                            <i class="ace-icon fa fa-plus blue"></i>
                            更新排序
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->

        <div id="course-content-modal" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"></button>
                        <h4 class="modal-title">内容编辑</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <div class="col-lg-12">
                                   {{saveContentLabel}}
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-lg-12">
                                    <div id="content"></div>
                                </div>
                            </div>
                        </form>
                    </div>
                    <button type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">
                        <i class="ace-icon fa fa-times"></i>
                        取消
                    </button>&nbsp;
                    <button type="button" class="btn btn-white btn-info btn-round" v-on:click="saveContent()">
                        <i class="ace-icon fa fa-plus blue"></i>
                        保存
                    </button>&nbsp;
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import Pagination from "../../components/pagination";
    export default {
        components: {Pagination},
        name: "business-course",
        data :function(){
            return {
                course: {},
                courses : [],
                COURSE_CHARGE:COURSE_CHARGE,
                COURSE_LEVEL:COURSE_LEVEL,
                COURSE_STATUS:COURSE_STATUS,
                categorys: [],
                tree:{},
                saveContentLabel:"",
                sort: {
                    id: "",
                    oldSort: 0,
                    newSort: 0
                },
                teachers:[],
            }
        },
        mounted: function () {
            // this.$parent.activeSidebar("business-course-sidebar");
            let _this=this;
            _this.$refs.pagination.size=5;
            _this.allCategory();
            _this.allTeacher();
            _this.list(1);
        } ,
        methods:{
            /**
             * 点击【新增】
             */
            add() {
                let _this = this;
                _this.course = {
                    sort: _this.$refs.pagination.total + 1
                };
                _this.tree.checkAllNodes(false);
                $("#form-modal").modal("show");
            },
            edit(course) {
                let _this = this;
                _this.course = $.extend({},course);
                _this.listCategory(course.id);
                $("#form-modal").modal("show");
            },
            list(page){
                let _this = this;
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/list',{
                    page:page,
                    size:_this.$refs.pagination.size,
                }).then((response) => {
                    console.log("查询课程列表结果：", response);
                    let resp=response.data;
                    _this.courses=resp.content.list;
                    _this.$refs.pagination.render(page,resp.content.total);
                })
            },
            save(){
                let _this = this;

                // 保存校验
                if (1 != 1
                    || !Validator.require(_this.course.name, "名称")
                    || !Validator.length(_this.course.name, "名称", 1, 50)
                    || !Validator.length(_this.course.summary, "概述", 1, 2000)
                    || !Validator.require(_this.course.price, "价格（元）")
                    || !Validator.length(_this.course.image, "封面", 1, 100)
                    || !Validator.require(_this.course.level, "级别")
                ) {
                    return;
                }

                let categorys = _this.tree.getCheckedNodes();
                if (Tool.isEmpty(categorys)){
                    Toast.warning("请选择你的分类");
                    return;
                }
                console.log(categorys);
                _this.course.categorys=categorys;
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/save',
                    _this.course).then((response) => {
                    console.log("保存课程列表结果：", response);
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
                Confirm.show("删除课程后不可恢复，是否删除？",function () {
                    _this.$ajax.delete(process.env.VUE_APP_SERVER + '/business/admin/course/delete/' + id).then((response) => {
                        console.log("删除课程列表结果：", response);
                        let resp=response.data;
                        if (resp.success){
                            _this.list(1);
                            Toast.success("删除成功!")
                        }
                    })
                });
            },
            allCategory() {
                let _this = this;
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/category/all').then((response)=>{
                    let resp = response.data;
                    _this.categorys = resp.content;

                    _this.initTree();
                })
            },

            initTree() {
                let _this = this;
                let setting = {
                    check: {
                        enable: true
                    },
                    data: {
                        simpleData: {
                            idKey: "id",
                            pIdKey: "parent",
                            rootPId: "00000000",
                            enable: true
                        }
                    }
                };

                let zNodes = _this.categorys;

                _this.tree = $.fn.zTree.init($("#tree"), setting, zNodes);
                // 展开所有的节点
               // _this.tree.expandAll(true);
            },
            /**
             * 查找课程下所有分类
             * @param courseId
             */
            listCategory(courseId) {
                let _this = this;
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/list-category/' + courseId).then((res)=>{
                    console.log("查找课程下所有分类结果：", res);
                    let response = res.data;
                    let categorys = response.content;

                    // 勾选查询到的分类
                    _this.tree.checkAllNodes(false);
                    for (let i = 0; i < categorys.length; i++) {
                        let node = _this.tree.getNodeByParam("id", categorys[i].categoryId);
                        _this.tree.checkNode(node, true);
                    }
                })
            },
            editContent(course) {
                let _this = this;
                let id = course.id;
                _this.course =course;
                $("#content").summernote({
                    focus: true,
                    height: 300
                });
                // 先清空历史文本
                $("#content").summernote('code', '');
                _this.saveContentLabel="";
                _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/admin/course/find-content/' + id).then((response)=>{
                    let resp = response.data;

                    if (resp.success) {
                        $("#course-content-modal").modal({backdrop:'static',keyboard:false});
                        if (resp.content) {
                            $("#content").summernote('code', resp.content.content);
                        }
                        // 定时自动保存
                        _this.saveContentInterval = setInterval(function() {
                            _this.saveContent();
                        }, 5000);
                    } else {
                        Toast.warning(resp.message);
                    }
                });
            },

            /**
             * 保存内容
             */
            saveContent () {
                let _this = this;
                let content = $("#content").summernote("code");
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/save-content', {
                    id: _this.course.id,
                    content: content
                }).then((response)=>{
                    let resp = response.data;
                    if (resp.success) {
                        // Toast.success("内容保存成功");
                        let now = Tool.dateFormat("mm:ss");
                        _this.saveContentLabel = "最后保存时间：" + now;
                    } else {
                        Toast.warning(resp.message);
                    }
                });
            },
            openSortModal(course) {
                let _this = this;
                _this.sort = {
                    id: course.id,
                    oldSort: course.sort,
                    newSort: course.sort
                };
                $("#course-sort-modal").modal("show");
            },
            /**
             * 排序
             */
            updateSort() {
                let _this = this;
                if (_this.sort.newSort === _this.sort.oldSort) {
                    Toast.warning("排序没有变化");
                    return;
                }
                _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/course/sort", _this.sort).then((res) => {
                    let response = res.data;

                    if (response.success) {
                        Toast.success("更新排序成功");
                        $("#course-sort-modal").modal("hide");
                        _this.list(1);
                    } else {
                        Toast.error("更新排序失败");
                    }
                });
            },
            allTeacher() {
                let _this = this;
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/teacher/all').then((response)=>{
                    let resp = response.data;
                    _this.teachers = resp.content;
                })
            },

            /**
             * 点击【大章】
             */
            toChapter(course) {
                let _this = this;
                SessionStorage.set(SESSION_KEY_COURSE, course);
                _this.$router.push("/business/chapter");
            }
        }
    }
</script>

<style scoped>
    .caption h3 {
        font-size: 20px;
    }
    @media (max-width: 1199px) {
        .caption h3 {
            font-size: 16px;
        }
    }
</style>

