import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/admins',
    component: () => import('@/views/admins/layout/Layout.vue'),
    redirect: '/admins/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/admins/dashboard/Dashboard.vue'),
        meta: { title: '数据概览' }
      },
      {
        path: 'user',
        name: 'user',
        component: () => import('@/views/admins/user/User.vue'),
        meta: { title: '用户' }
      },
      {
        path: 'article',
        name: 'article',
        component: () => import('@/views/admins/article/Article.vue'),
        meta: { title: '文章列表' }
      },
      {
        path: 'article/tag',
        name: 'articleTag',
        component: () => import('@/views/admins/article/ArticleTag.vue'),
        meta: { title: '文章标签' }
      },
      {
        path: 'article/type',
        name: 'articleType',
        component: () => import('@/views/admins/article/ArticleType.vue'),
        meta: { title: '文章类型' }
      },
      {
        path: 'link',
        name: 'link',
        component: () => import('@/views/admins/link/Link.vue'),
        meta: { title: '友情链接' }
      },
      {
        path: 'ad',
        name: 'ad',
        component: () => import('@/views/admins/ad/Ad.vue'),
        meta: { title: '广告管理' }
      },
      {
        path: 'admin',
        name: 'admin',
        component: () => import('@/views/admins/admin/Admin.vue'),
        meta: { title: '管理员' }
      }
    ]
  },
  {
    path: '/users',
    component: () => import('@/views/users/layout/Layout.vue'),
    children: [
      {
        path: '',
        name: 'UserHome',
        component: () => import('@/views/users/homepage/HomePage.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'article/:articleId',
        name: 'ArticleDetail',
        component: () => import('@/views/users/article/ArticleDetails.vue'),
        meta: { title: '文章详情' }
      },
      {
        path: 'sidebar',
        name: 'Sidebar',
        component: () => import('@/views/users/sidebar/Sidebar.vue'),
        meta: { title: '侧边栏' }
      },
      {
        path: 'write',
        name: 'Write',
        component: () => import('@/views/users/write/Write.vue'),
        meta: { title: '写文章' }
      },
      {
        path: 'userLayout',
        name: 'UserLayout',
        component: () => import('@/views/users/personal/UserLayout.vue'),
        children: [
          {
            path: '',
            redirect: to => {
              const loginData = localStorage.getItem('login')
              if (loginData) {
                const login = JSON.parse(loginData)
                return `/users/userLayout/profile/${login.id}`
              }
              return '/login'
            }
          },
          {
            path: 'profile/:userId',
            name: 'UserProfile',
            component: () => import('@/views/users/personal/Profile.vue'),
            meta: { title: '我的主页' }
          },
          {
            path: 'following/:userId',
            name: 'UserFollowing', 
            component: () => import('@/views/users/personal/Following.vue'),
            meta: { title: '我的关注' }
          },
          {
            path: 'followers/:userId',
            name: 'UserFollowers',
            component: () => import('@/views/users/personal/Followers.vue'),
            meta: { title: '我的粉丝' }
          },
          {
            path: 'myArticles/:userId',
            name: 'MyArticles',
            component: () => import('@/views/users/personal/MyArticles.vue'),
            meta: { title: '我的文章' }
          }
        ]
      },
      {
        path: 'profile/:userId',
        name: 'Profile',
        component: () => import('@/views/users/personal/Profile.vue'),
        meta: { title: '主页' }
      },
      {
        path: 'categories/:typeId',
        name: 'CategoryDetail',
        component: () => import('@/views/users/categories/Categories.vue'),
        meta: { title: '分类详情' }
      },
      {
        path: 'about',
        name: 'About',
        component: () => import('@/views/users/about/About.vue'),
        meta: { title: '关于' }
      },
      
      {
        path: 'search',
        name: 'Search',
        component: () => import('@/views/users/search/Search.vue'),
        meta: { title: '搜索' }
      }
    ]
  },
  {
    path: '/login',
    name: 'loginAdmin',
    component: () => import('@/views/login/Login.vue'),
    meta: { title: '管理员登录界面' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router