package controllers;


import models.Employees;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

import static play.libs.Json.toJson;

public class EmployeesController extends Controller
{
    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public EmployeesController(FormFactory formFactory, JPAApi jpaApi)
    {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    @Transactional(readOnly = true)
    public Result getEmployees()
    {
        List<Employees>employees = (List<Employees>)jpaApi.em().
                createQuery("select employeeId, firstName, lastName from Employees").getResultList();

        return ok(toJson(employees));
    }

    @Transactional(readOnly = true)
    public Result index()
    {
        List<Employees> employees= (List<Employees>) jpaApi.em().
                createQuery("select e from Employees e").getResultList();

        return ok(views.html.employees.render(employees));

    }

    @Transactional(readOnly = true)
    public Result getPicture(Long id)
    {
        Employees employee = (Employees)jpaApi.em().
                createQuery("select e from Employees e where employeeId = :id").
                setParameter("id", id).getSingleResult();

        if (employee.photo == null )
        {
            return null;
        }
        else
        {
            return ok(employee.photo).as("image/bmp");
        }
    }
    @Transactional(readOnly = true)
    public Result editEmployee(Long employeeId)
    {
        //Get the model that we need to edit based on the passed in categoryId
        Employees employee = (Employees) jpaApi.em()
                .createQuery("select e from Employees e where employeeId = :id")
                .setParameter("id", employeeId).getSingleResult();

        //Send the model we got to the update view
        return ok(views.html.updateEmployee.render(employee));
    }

    @Transactional
    public Result updateEmployee()
    {
        //Gets the data from the form the user submitted
        DynamicForm postedForm = formFactory.form().bindFromRequest();

        //Copy the form values out into local variables
        Long employeeId = new Long(postedForm.get("employeeId"));
        String firstName = postedForm.get("firstName");
        String lastName = postedForm.get("lastName");

        //Get the model of the category we want to update
        Employees employee = (Employees) jpaApi.em()
                .createQuery("select e from Employees e where employeeId = :id")
                .setParameter("id", employeeId).getSingleResult();

        employee.firstName = firstName;
        employee.lastName =lastName;


        jpaApi.em().persist(employee);

        return redirect(routes.EmployeesController.index());

    }

}

