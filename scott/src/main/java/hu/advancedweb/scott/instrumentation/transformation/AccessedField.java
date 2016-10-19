package hu.advancedweb.scott.instrumentation.transformation;

class AccessedField {
	
	final String owner;
	final String name;
	final String desc;
	final boolean isStatic;
	
	public AccessedField(String owner, String name, String desc, boolean isStatic) {
		this.owner = owner;
		this.name = name;
		this.desc = desc;
		this.isStatic = isStatic;
	}

	@Override
	public String toString() {
		return "AccessedField [owner=" + owner + ", name=" + name + ", desc=" + desc + ", isStatic=" + isStatic + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccessedField other = (AccessedField) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
