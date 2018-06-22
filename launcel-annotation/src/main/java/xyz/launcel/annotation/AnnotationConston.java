package xyz.launcel.annotation;

public interface AnnotationConston
{
    String point = "@annotation(xyz.launcel.annotation.ValidateMethod)";

    String mapping = "@annotation(xyz.launcel.annotation.ValidateMethod)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)";
}
