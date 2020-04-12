package top.gmfcj.fallback;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ServiceFallbackProvider implements FallbackProvider {
    @Override
    public String getRoute() {
        // 返回一个服务id,指定为哪一个服务提供服务回退
        // 也可以使用*代表对所有的失败服务进行回退
        return "*";
    }

    /**
     * 这个方法需要返回一个ClientHttpResponse对象
     * ClientHttpResponse是一个接口，具体的回退逻辑要实现此接口
     * @param route 出错的微服务名
     * @param cause 出错的异常对象
     * @return
     */
    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        // 这里可以根据不同的异常类型来做相应的处理
        // 完了之后调用response方法并根据异常类型传入HttpStatus
        if (cause instanceof HystrixTimeoutException) {
            return response(HttpStatus.GATEWAY_TIMEOUT);
        } else {
            return response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ClientHttpResponse response(HttpStatus httpStatus) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                // 返回一个 HttpStatus 对象,这是一个枚举对象,枚举了HTTP响应可能出现的基本类型
                return httpStatus;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                // 返回异常状态码
                return httpStatus.value();
            }

            @Override
            public String getStatusText() throws IOException {
                // 返回异常信息
                return httpStatus.getReasonPhrase();
            }

            @Override
            public void close() {
                // 当降级信息相应完成之后,执行close方法
            }

            @Override
            public InputStream getBody() throws IOException {
                // 设置返回浏览器的body
                return new ByteArrayInputStream("自己随便写的降级信息...".getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                // 如果需要设置相应的头信息,可以在这里设置
                // 比如返回json格式或者流格式 等等...
                HttpHeaders header = new HttpHeaders();
                // 设置响应为json格式
                header.setContentType(MediaType.APPLICATION_JSON);
                return header;
            }
        };
    }
}
