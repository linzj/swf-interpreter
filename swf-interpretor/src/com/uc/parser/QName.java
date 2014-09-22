package com.uc.parser;

public class QName {
	String package_name;
	String name;

	public QName(String package_name, String name) {
		this.package_name = package_name;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Qname(PackageNamespace(\"" + package_name + "\"), \"" + name
				+ "\")";
	}

	@Override
	public boolean equals(java.lang.Object obj) {
		if (super.equals(obj))
			return true;
		if (!(obj instanceof QName))
			return false;
		QName other = (QName) obj;
		if (other.package_name.equals(package_name) && other.name.equals(name))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		return package_name.hashCode() ^ name.hashCode();
	}
}
