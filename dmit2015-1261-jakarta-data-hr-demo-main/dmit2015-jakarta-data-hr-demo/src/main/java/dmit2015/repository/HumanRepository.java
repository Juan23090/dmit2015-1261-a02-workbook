package dmit2015.repository;

import dmit2015.entity.Department;
import dmit2015.entity.Employee;
import dmit2015.entity.Region;
import jakarta.data.repository.*;

import java.util.List;

@Repository
public interface HumanRepository {

    //1. CURD Operation---we dont need for assigemt

    //insert a new Region into the database

    @Insert
    Region addRegion(Region newRegion);

    //update a region
    @Update
    Region updateRegion(Region updateRegion);

    @Delete
    void deleRegion(Region exsitingRegion);


    //2. find operation

    //find a deparment using it primary key

    @Find
    Department departmentByDepartmentId(Short id);

    //find region using primary key

    @Find
    Region findRegionById(Long id);

    //retrive all region recorrds

    @Find
    List<Region> findAllRegion();

    //3. Sorting with orderby


    //retrive all region sorted by resgionname

    @Find
    @OrderBy("regionName")
    List<Region> findALlRegionOrderByRegionName();


    //4.Csutom Qurey---needed for Assigebment

    //search department by name

    @Query("""
 select d from Department d where lower(d.departmentName) like lower(?1) order by d.departmentName """ )

        List<Department> departmentsBy(String departmentName);

    //retivre employees for a department
    //join fetch loads department
    @Query("""
select e
from Employee e join fetch e.department join fetch e.job join fetch e.manager
where e.department.id = ?1
""")
    List<Employee> employeesByDepartmentId(Short id);

}


