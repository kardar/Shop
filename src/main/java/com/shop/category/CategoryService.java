package com.shop.category;

import com.shop.entity.Catogry;
import net.bytebuddy.TypeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoryService {

    private static final int ROOT_PER_PAGE = 4;

    @Autowired
    private CategoryRepository repo;

    public List<Catogry> listByPage(CategoryPageInfo pageInfo, int pageNum,String sortDir){
        Sort sort = Sort.by("name");
         if (sortDir.equals("asc")){
            sort = sort.descending();
        } else if (sortDir.equals("desc")){
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(pageNum-1,ROOT_PER_PAGE,sort);

        Page<Catogry> pageCategories = repo.findRootCategory(pageable);
        List<Catogry> rootCategories = pageCategories.getContent();

        pageInfo.setTotalElements((int) pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());

        return listHirarechalCategories(rootCategories,sortDir);

    }


    private List<Catogry> listHirarechalCategories(List<Catogry> rootCategories,String sortDir){
        List<Catogry> hirarechalCategories = new ArrayList<>();
        for (Catogry rootCategory : rootCategories){

            hirarechalCategories.add(Catogry.copyFull(rootCategory));

            Set<Catogry> children = sortSubCategories(rootCategory.getChildren(),sortDir);
            for (Catogry subCategory : children){
                String name = "---"+subCategory.getName();
                hirarechalCategories.add(Catogry.copyFull(subCategory, name));
                listSubHierarichalCategories(subCategory,1,hirarechalCategories,sortDir);
            }
        }

        return hirarechalCategories;
    }

    private void listSubHierarichalCategories(Catogry parent, int subLevel,List<Catogry> hirarechalCategories,String sortDir){
        Set<Catogry> children = sortSubCategories(parent.getChildren(),sortDir);

        for (Catogry subCategory : children){
            int newSubLevel = subLevel+1;
            String name = "";
            for (int i = 0; i < newSubLevel; i++){
                name += "--";
            }
            name = subCategory.getName();
            hirarechalCategories.add(Catogry.copyFull(subCategory, name));
            listSubHierarichalCategories(subCategory,newSubLevel,hirarechalCategories,sortDir);
        }
    }

    public Catogry save(Catogry catogry){
        return repo.save(catogry);
    }

    public List<Catogry> getCategoriesUsedInForm(){
        List<Catogry> categoryUsedInForm = new ArrayList<>();

        Iterable<Catogry> categoryUsedInDb = repo.findRootCategory(Sort.by("name").ascending());

        for (Catogry catogry : categoryUsedInDb){
            if (catogry.getParent() == null){
               categoryUsedInForm.add(Catogry.copyIdAndName(catogry));
            }
            Set<Catogry> children = sortSubCategories(catogry.getChildren());
            for (Catogry subCategory : children){
                String name = "---"+subCategory.getName();
                categoryUsedInForm.add(Catogry.copyIdAndName(subCategory.getId(),name));
                printChildren( categoryUsedInForm,subCategory,1);
            }
        }

        return categoryUsedInForm;
    }

    private SortedSet<Catogry> sortSubCategories(Set<Catogry> children){
       return sortSubCategories(children,"asc");
    }

    private SortedSet<Catogry> sortSubCategories(Set<Catogry> children,String sortDir){
        SortedSet<Catogry> sortedSet = new TreeSet<>(new Comparator<Catogry>() {
            @Override
            public int compare(Catogry catogry1, Catogry catogry2) {
                if (sortDir.equals("asc")){
                    return catogry1.getName().compareTo(catogry2.getName());
                }else {
                    return catogry2.getName().compareTo(catogry1.getName());
                }

            }
        });
        sortedSet.addAll(children);
        return sortedSet;
    }

    public void printChildren(List<Catogry> categoryUsedInForm,Catogry parent, int subLevel){
        int newSubLevel = subLevel+1;
        Set<Catogry> children = sortSubCategories( parent.getChildren());
        for (Catogry subCategory : children){
            String name = "";
            for (int i = 0; i < newSubLevel; i++){
                name += "--";
            }
            name = subCategory.getName();
            categoryUsedInForm.add(Catogry.copyIdAndName(subCategory.getId(),name));
            printChildren( categoryUsedInForm,subCategory,newSubLevel);

        }
    }

    public Catogry get(Integer id){
        try{
            return repo.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new NoSuchElementException("Could not find any Category with ID "+id);
        }

    }
    public String checkUniqe(Integer id, String name, String alias){

        boolean isCreatingNew = (id == null || id == 0);
        Catogry categoryByName = repo.findByName(name);
        if (isCreatingNew){
            if (categoryByName != null){
                return "Duplicate";
            }else {
                Catogry catogryByAlias = repo.findByAlias(alias);
                if (catogryByAlias != null){
                    return "Dublicate Alias";
                }
            }
        }else {
            if (categoryByName != null && categoryByName.getId() != id){
                return "DuplicateName";
            }
            Catogry catogryByAlias = repo.findByAlias(alias);
            if (catogryByAlias != null && catogryByAlias.getId() != id){
                return "DuplicateAlias";
            }
        }

        return "ok";
    }

    public void updateCategoryEnabledStatus(Integer id, boolean enabled){
           repo.updateEnabledStatus(id,enabled);
    }

    public void delete(Integer id){
        Long countById = repo.countById(id);
        if (countById == null || countById == 0){
            throw new NoSuchElementException("No Category found of ID "+id);
        }
        repo.deleteById(id);
    }
}
