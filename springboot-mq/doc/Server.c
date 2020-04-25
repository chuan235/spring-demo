#include<stdio.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>	
#include "com_Server.h"

JNIEXPORT void JNICALL Java_com_Server_conntect(JNIEnv* env, jclass c1){
	// 调用系统socket函数 <- sys/socket.h  lfd:文件描述符
	int lfd = socket(AF_INET,SOCK_STREAM,0); //SOCK_STREAM 表示协议，TCP/IP man 2 socket
	struct sockaddr_in my_addr; 
	my_addr.sin_family = AF_INET;				// ipv4
	my_addr.sin_port   = htons(8080);			// 端口，转为字节
    // INADDR_ANY，表示任意一个可用的网卡地址
	my_addr.sin_addr.s_addr = htonl(INADDR_ANY);// 将127.0.0.1转为ip地址，->字节数组
    // 传入参数，传出参数，传入传出参数
    // 文件描述符，socket socketaddr/socket_in() ip长度
	bind(lfd, (struct sockaddr*)&my_addr, sizeof(my_addr));// man 2 bind
    // 同一时刻可以监听的请求数，最多在一秒内可以监听到128个请求
	listen(lfd, 10);
	printf("listen client @port=%d...\n",8080);
    
	struct sockaddr_in client_addr;		   // 定义就可以
	char cli_ip[INET_ADDRSTRLEN] = "";	   // INET_ADDRSTRLEN 宏定义
	socklen_t cliaddr_len = sizeof(client_addr);    
	int connfd = 0;
    // 创建socket对应文件描述符 client_addr是一个传出参数 客户端地址长度->传入传出参数
	connfd = accept(lfd, (struct sockaddr*)&client_addr, &cliaddr_len);// man 2 accept
    
    // 将结构体转为标准的ip和port，转到cli_ip这个数组中
	inet_ntop(AF_INET, &client_addr.sin_addr, cli_ip, INET_ADDRSTRLEN);
    printf("----------------------------------------------\n");
	printf("client ip=%s,port=%d\n", cli_ip,ntohs(client_addr.sin_port));// 客户端也存在一个端口号，内核隐式分配
	char recv_buf[512] = "";
	while(1) // 一直读
	{	// 读数据到recv_buf
        int k = read(connfd, recv_buf, sizeof(recv_buf));
        printf("recv data=%d\n",k);
        printf("%s\n",recv_buf);
	}
    close(connfd);
    printf("client closed!\n");
    close(lfd); 
}

int main(){
	return 0;
}
