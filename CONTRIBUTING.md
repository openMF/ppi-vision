## How to Contribute

### Raising an issue:

This is an Open Source project and we would be happy to see contributors who report bugs and file feature requests submitting pull requests as well.
This project adheres to the Contributor Covenant code of conduct.
By participating, you are expected to uphold this code style.
Please report issues here [Issues - openMF/ppi-vision](https://github.com/openMF/ppi-vision/issues)

### Branch Policy

#### Sending pull requests:

Go to the repository on GitHub at https://github.com/openMF/ppi-vision .

Click the “Fork” button at the top right.

You’ll now have your own copy of the original ppi-vision repository in your GitHub account.

Open a terminal/shell.

Type

`$ git clone https://github.com/username/ppi-vision`

where 'username' is your username.

You’ll now have a local copy of your version of the original ppi-vision repository.

#### Change into that project directory (ppi-vision):

`$ cd ppi-vision`

#### Add a connection to the original ppi-vision repository.

`$ git remote add upstream https://github.com/openMF/ppi-vision`

#### To check this remote add set up:

`$ git remote -v`

#### Make changes to files.

`git add` and `git commit` those changes

`git push` them back to GitHub. These will go to your version of the repository.


#### Now Create a PR (Pull Request)

Go to your version of the repository on GitHub.

Click the “New pull Request” button at the top.

Click the green button “Create pull request”. Give a succinct and informative title, in the comment field give a short explanation of the changes and click the blue button “Create pull request” again.

#### Pulling others’ changes

Before you make further changes to the repository, you should check that your version is up to date relative to original version.

Go into the directory for the project and type:

`$ git checkout <branch under development>`

`$ git pull upstream master --rebase`

This will pull down and merge all of the changes that have been made in the original ppi-vision repository.

Now push them to your GitHub repository.

`$ git push origin <branch under development>` 
