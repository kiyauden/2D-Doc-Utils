package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;
import static fr.kiyauden._2ddoc.Document.DOC_01;
import static org.hamcrest.MatcherAssert.assertThat;

class DocumentTest {

    @Test
    void findById_whenDataWithIdExists_shouldReturnIt() {
        final Optional<Document> document = Document.findById("01");
        assertThat(document, isPresentAndIs(DOC_01));
    }

    @Test
    void findById_whenDataWithIdDoesNotExists_shouldReturnEmptyOptional() {
        final Optional<Document> document = Document.findById("EE");
        assertThat(document, isEmpty());
    }

}
