package sk.esten.uss.gbco2.utils

import javax.persistence.criteria.Path
import javax.persistence.metamodel.SingularAttribute
import org.springframework.data.jpa.domain.Specification

fun <T, W> List<W>.idsInSpecification(attr: SingularAttribute<T, W>): Specification<T> {
    return Specification { root, _, builder ->
        builder.or(
            *this.asSequence().chunked(999).map { root.get(attr).`in`(it) }.toList().toTypedArray()
        )
    }
}

fun <T, W> List<W>.idsInSpecification(path: Path<W>): Specification<T> {
    return Specification { _, _, builder ->
        builder.or(*this.asSequence().chunked(999).map { path.`in`(it) }.toList().toTypedArray())
    }
}
