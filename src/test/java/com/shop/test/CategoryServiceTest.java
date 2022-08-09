package com.shop.test;

import com.shop.category.CategoryRepository;
import com.shop.category.CategoryService;
import com.shop.entity.Catogry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

    @MockBean
    private CategoryRepository repo;

    @InjectMocks
    private CategoryService service;


    @Test
    public void testUniqueInNewModeAndReturnDublicate(){
        Integer id = null;
        String name = "Computer";
        String alias = "abc";
        Catogry catogry = new Catogry(id,name,alias);

        Mockito.when(repo.findByName(name)).thenReturn(catogry);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);
        String result = service.checkUniqe(id, name, alias);
        assertThat(result).isEqualTo("Dublicate Alias");
    }

    @Test
    public void testUniqueInNewModeAndReturnDublicateAlias(){
        Integer id = null;
        String name = "Nameabc";
        String alias = "Computer";
        Catogry catogry = new Catogry(id,name,alias);

        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(catogry);
        String result = service.checkUniqe(id, name, alias);
        assertThat(result).isEqualTo("Dublicate Alias");
    }

    @Test
    public void testUniqueInNewModeAndReturnOk(){
        Integer id = null;
        String name = "Nameabc";
        String alias = "Computer";
        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);
        String result = service.checkUniqe(id, name, alias);
        assertThat(result).isEqualTo("ok");
    }

}
