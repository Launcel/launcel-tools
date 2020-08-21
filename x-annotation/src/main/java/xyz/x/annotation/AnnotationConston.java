package xyz.x.annotation;

public interface AnnotationConston
{

    String point = "@annotation(xyz.launcel.annotation.ParamValidate)";

    String mapping = "@annotation(xyz.launcel.annotation.ParamValidate)"
            + "|| @annotation(org.springframework.web.bind.annotation.GetMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.PostMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)";

    String dataSource = "@annotation(xyz.launcel.annotation.DataSource)";
}
