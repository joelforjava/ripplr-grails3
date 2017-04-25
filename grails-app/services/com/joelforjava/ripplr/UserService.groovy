package com.joelforjava.ripplr

import grails.transaction.Transactional

class UserException extends RuntimeException {
	String message
	User user
}


@Transactional
class UserService {

    /**
     * Retrieve a user via unique ID.
     *
     * @param id - the user ID as assigned by the database.
     * @return the retrieved user.
     * @throws UserException if user is not found
     */
    User retrieveUser(Long id) {
        def user = User.get(id)

        if (!user) {
            throw new UserException(message: "User not found ($id)")
        }

        user
    }

    /**
     * Retrieve a user with a given unique username.
     *
     * @param username - the username of given user.
     * @return the retrieved user.
     * @throws UserException if user is not found
     */
    User retrieveUser(String username) {
        def user = User.findByUsername(username)

        if (!user) {
            throw new UserException(message: "User not found ($username)")
        }

        user
    }

    /**
     * Retrieve the latest users that have joined.
     *
     * @param numLatest - the number of requested users
     * @return a list of at most the numLatest users.
     * @throws UserException if no users are found
     */
    List retrieveLatestUsers(int numLatest) {
        int numToReturn = 5
        if (numLatest > 0) {
            numToReturn = numLatest
        }

        def users = User.list(max: numToReturn, sort: 'dateCreated', order: 'desc')

        if (!users) {
            throw new UserException(message: 'Latest users not found!')
        }

        users
    }

    User createUser(String username, String passwordHash, boolean accountLocked, boolean accountExpired,
    			 boolean passwordExpired) {

        // TODO-generate hashed password here and save it
        // remove encryption logic from the User domain class!
    	def user = new User(username: username, passwordHash: passwordHash, 
    						accountLocked: accountLocked, accountExpired: accountExpired, 
    						passwordExpired: passwordExpired)
        user.save()
        user
    }

    User createUser(String username, String passwordHash) {
    	this.createUser(username, passwordHash, false, false, false)
    }

    User createUserAndProfile(UserRegisterCommand userRegistration) {
        def user = createUser(userRegistration.username, userRegistration.password)
        user.profile = new Profile(userRegistration.profile.properties)
        user.save()
        user
    }

    User saveUser(userId, String username, String passwordHash, boolean accountLocked, boolean accountExpired,
                 boolean passwordExpired) {

        def user = retrieveUser userId

        user.passwordHash = passwordHash
        user.accountLocked = accountLocked
        user.accountExpired = accountExpired
        user.passwordExpired = passwordExpired
        user.username = username // this will be turned off at the UI level, for now
        user.save()

        user
    }

    User saveUser(userId, String username, String passwordHash) {
        this.saveUser(userId, username, passwordHash, false, false, false)
    }

    User updateUsername(userId, String username) {
        def user = retrieveUser userId

        user.username = username
        user.save()

        user

    }

    // For future development and current testing
    int onlineUserCount() {
        return User.count()
    }

    boolean addToFollowing(String username, String nameToFollow) {
        def user = User.findByUsername(username)
        def userToFollow = User.findByUsername nameToFollow

        if (user && userToFollow && (userToFollow.username != user.username)) {
            log.debug "userToFollow valid and username is not current user's username. attempting to add"
            user.addToFollowing userToFollow
            def success = user.save()
            def following = user.following
            log.debug "${user.username} is following $following"
            return success
        } else {
            throw new UserException(message: "Either requested user to follow is invalid or that user is equal to the user requesting to add", user: user)
        }
        return false
    }

    boolean removeFromFollowing(String username, String nameToUnfollow) {
        def user = User.findByUsername username
        def userToRemove = User.findByUsername nameToUnfollow

        if (user && userToRemove && (userToRemove.username != user.username)) {
            user.removeFromFollowing userToRemove
            def success = user.save()
            def following = user.following
            log.debug "${user.username} is now following $following"
            return success
        } else {
            throw new UserException(message: "Either requested user to remove from following is invalid or user is attempting to unfollow himself", user: user)
        }
        return false
    }

    boolean addToBlocking(String username, String nameToBlock) {
        def user = User.findByUsername username
        def userToBlock = User.findByUsername nameToBlock

        // Do we add logic to remove nameToBlock from user.following?
        // Or, is that handled separately? -- probably separately. Don't want one method doing too much

        if (user && userToBlock && (userToBlock.username != user.username)) {
            user.addToBlocking userToBlock
            def success = user.save()
            return success
        } else {
            throw new UserException("Either requested user to block is invalid or that user is equal to the user requesting to block", user: user)
        }
        return false
    }

    def getFollowedByForUser(String username) {
        def user = retrieveUser username
        if (user) {
            def query = User.where {
                following*.username == username
            }
            query.list()
        } else {
            []
        }
    }

    def getBlockedByOthersForUser(String username) {
        def user = retrieveUser username
        if (user) {
            def query = User.where {
                blocking*.username == username
            }
            query.list()
        } else {
            []
        }
    }

}
