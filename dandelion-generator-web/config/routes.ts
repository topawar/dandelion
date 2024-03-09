export default [
  {path: '/user', layout: false, routes: [{path: '/user/login', component: './User/Login'}]},
  {path: '/', icon: 'smile', component: './Index', name: "首页"},
  {
    path: '/admin',
    icon: 'crown',
    name: "管理页",
    access: 'canAdmin',
    routes: [
      {path: '/admin', redirect: '/admin/user'},
      {icon: 'table', path: '/admin/user', component: './Admin/User', name: "用户管理"},
      {icon: 'table', path: '/admin/generator', component: './Admin/Generator', name: "生成器管理"}
    ],
  },
  {path: '/', redirect: '/index'},
  {path: '*', layout: false, component: './404'},
];
