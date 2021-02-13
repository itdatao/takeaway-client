# 外卖

#### 介绍
毕设：点餐系统客户端后端代码

#### 软件架构
SpringBoot+MyBatis Plus+Spring MVC 
- 数据库：MySQL5.7
- 需要修改配置文件中数据库连接端口，用户名，密码，数据库名
- 支付宝配置支付宝公匙和应用私匙
- 微信登录需要配置AppID，secret，redirect_url
- 数据库连接池：Druid

#### 安装教程

1.  静态图片加载不出来，需要修改nginx配置文件，nginx.conf

    ```conf
    server {
        listen       8080;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location /takeaway/profile/ {
            alias D:/ruoyi/uploadPath/;
        }
        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        } 
    }

    server {
        listen       8150;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
	    proxy_pass  http://localhost:8088;
		}
     }
    }
```
2. 启动数据库，如果SpringBoot端口被占用，默认是8088，需要修改端口

#### 项目展示


#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
