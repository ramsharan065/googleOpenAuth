class BootStrap {

    def init = { servletContext ->
    	def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
    	def userRole = new Role(authority: 'ROLE_User').save(flush: true)

    	def testUser = new User(username: 'ramsharanshrestha@lftechnology.com', password: 'admins#1', fullname: 'Ramsharan Shrestha').save(flush: true)
    	def testAdmin = new User(username: 'prakashjoshi@lftechnology.com', password: 'admins#1', fullname: 'Prakash Chandra Joshi').save(flush: true)

    	UserRole.create testUser, userRole, true
    	UserRole.create testAdmin, adminRole, true

    	assert User.count() == 2
    	assert Role.count() == 2
    	assert UserRole.count() == 2 
    }
    def destroy = {
    }
}
