package com.uc.parser;

class QName {
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
}
