package com.joelforjava.ripplr

import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification

@Rollback
@Integration
class RippleServiceIntegrationSpec extends Specification {

	def rippleService

	/* createRipple _, _, _ tests */
	
	def "ripple service saves valid content to a ripple object and tags to tag objects"() {
		given: "valid content and tags"

		def content = "My first ripple"
		def tags = ['first','awesome','lookatme']
		
		and: "a valid user"
		
		def user = new User(username: "Sterling", passwordHash: "HashedPasswd").save(flush: true)

		when: "we call createRipple on the ripple service"

		rippleService.createRipple user.username, content, tags

		then: "we see a ripple has been created"

		Ripple.count() == old(Ripple.count()) + 1

		and: "we see the ripples and tags added to the user"
		User.get(user.id).ripples.size() == 1
		User.get(user.id).tags.size() == 3
		User.get(user.id).ripples[0].tags.size() == 3
	}
	
	def "ripple service saves valid content to a ripple object when no tags are present"() {
		given: "valid content"

		def content = "My first ripple"
		def tags = []
		
		and: "a valid user"

		def user = new User(username: "Sterling", passwordHash: "HashedPasswd").save(flush: true)

		when: "we call createRipple on the ripple service"

		rippleService.createRipple user.username, content, tags

		then: "we see a ripple has been created"

		Ripple.count() == old(Ripple.count()) + 1

		and: "we see the ripples and tags added to the user"
		user.ripples.size() == 1
		user.tags == null
		user.ripples[0].tags == null
	}

	
	def "ripple service throws exception when invalid username is used"() {
		given: "valid content and tags"

		def content = "Hey, another ripple!"
		def tags = ['hashtagz']

		and: "an invalid username"

		def invalidUsername = "invalid_"

		when: "we call createRipple on the ripple service"

		rippleService.createRipple invalidUsername, content, tags

		then: "an exception is thrown"

		thrown RippleException
	}

}
