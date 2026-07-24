package com.sporekart.platform.mapping;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DtoMapperTest {

    record TestDto(String id, String name) {}
    record TestEntity(String id, String name) {}

    private final DtoMapper<TestDto, TestEntity> mapper = new DtoMapper<>() {
        @Override
        public TestDto toDto(TestEntity entity) {
            return new TestDto(entity.id(), entity.name());
        }

        @Override
        public TestEntity toEntity(TestDto dto) {
            return new TestEntity(dto.id(), dto.name());
        }
    };

    @Test
    void testToDto() {
        TestEntity entity = new TestEntity("1", "Test");
        TestDto dto = mapper.toDto(entity);
        assertEquals("1", dto.id());
        assertEquals("Test", dto.name());
    }

    @Test
    void testToEntity() {
        TestDto dto = new TestDto("1", "Test");
        TestEntity entity = mapper.toEntity(dto);
        assertEquals("1", entity.id());
    }

    @Test
    void testToDtoList() {
        List<TestEntity> entities = List.of(
                new TestEntity("1", "A"),
                new TestEntity("2", "B"));
        List<TestDto> dtos = mapper.toDtoList(entities);
        assertEquals(2, dtos.size());
    }

    @Test
    void testMapList() {
        List<String> source = List.of("a", "b", "c");
        List<String> result = DtoMapper.mapList(source, s -> s.toUpperCase());
        assertEquals(List.of("A", "B", "C"), result);
    }
}
