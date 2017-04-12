package quasarchimaere.identitygenerator.core.model;

public class Identity {
    private String firstName;
    private String lastName;
    private String email;

    public Identity() {
    }

    public Identity(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //***** GETTER & SETTER ************
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identity identity = (Identity) o;

        if (firstName != null ? !firstName.equals(identity.firstName) : identity.firstName != null) return false;
        if (lastName != null ? !lastName.equals(identity.lastName) : identity.lastName != null) return false;
        return email != null ? email.equals(identity.email) : identity.email == null;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Identity{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
