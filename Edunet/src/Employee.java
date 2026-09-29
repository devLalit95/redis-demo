public class Employee {
    String name;
    int salary;
    int allowance;
    Employee(){


    }

    public void setName(String name) {
        this.name = name;
    }



    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setAllowence(int allowence) {
        this.allowance = allowence;
    }

    public  void setData(String name, String department, int salary, int allowence) {
        this.name = name;
        this.salary = salary;
    }

    public void calculateSalary(){
        setSalary(salary + (salary /100 * 20));
    }

    public void printData(){
        System.out.println("Name: " + name);
        System.out.println("Salary: " + salary);
    }
}
