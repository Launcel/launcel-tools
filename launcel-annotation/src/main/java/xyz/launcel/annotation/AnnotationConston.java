package xyz.launcel.annotation;

public interface AnnotationConston
{

    String paramValidatorPoint = "@annotation(xyz.launcel.annotation.Validate)";

    String point = "@annotation(xyz.launcel.annotation.ParamValidate)";

    String mapping = "@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)";
}
