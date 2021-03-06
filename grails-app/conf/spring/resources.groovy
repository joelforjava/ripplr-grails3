import com.joelforjava.ripplr.CustomCorsFilter
import com.joelforjava.ripplr.MarshallerRegistrar
import org.springframework.boot.web.servlet.FilterRegistrationBean

// Place your Spring DSL code here
beans = {
    ripplrMarshallerRegistrar(MarshallerRegistrar)

    customCorsFilter(CustomCorsFilter)

    corsFilter(FilterRegistrationBean) {
        filter = customCorsFilter
        order = 0
    }

}
