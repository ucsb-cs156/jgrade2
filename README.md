<a name="readme-top"></a>

<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

<!-- Project Overview -->

<h3 align="center">jGrade2</h3>

  <p align="center">
    An annotation library used to help autograde student assignments in Java for Gradescope.
    <br />
    <a href="https://dscpsyl.github.io/jgrade2/javadoc/"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/dscpsyl/jgrade2/issues">Report Bug</a>
    ·
    <a href="https://github.com/dscpsyl/jgrade2/issues">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

This is an update to the original jGrade, now supporting Java17 and [JUnit5](https://junit.org/junit5/). This provides four 
annotations: `@Grade` (+ `@before` and `@after`) and `@GradedTest`, each meant to help autograde 
student assignments in Java for the Gradescope autograder. When correctly setup, instructors can 
simply use JUnit5 to write tests for assignemnts. This library will automatically capture results, 
and output the correct json format for Gradescope to read.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!--! GETTING STARTED -->
## Getting Started

### Prerequisites

jGrade2 requires Java 17 and JUnit5. It is recommended to use Maven to manage dependencies. Additional 
dependencies are listed in the `pom.xml` file. For the user's convenience, a `jar` file is provided
containing all the dependencies.

### Installation

There are two ways to install jGrade2.

1. Install directly from GitHub [releases](https://github.com/dscpsyl/jgrade2/releases). You can download
   either `jar` files or build the source code from scratch. If building from source, this project uses
   maven wrapper to build. Simply run `./mvnw clean package` to build the project. Two jar files will be
   created, both reflected on the [releases](https://github.com/dscpsyl/jgrade2/releases) page.
2. Add as a dependency directly from Maven Central. *This is not yet supported, but will be in the future...*

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!--! USAGE EXAMPLES -->
## Usage

Usage is nearly identical to the origional jGrade. *More information will be added soon...*

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* This is an update fork of the original [jGrade](https://github.com/tkutcher/jgrade).
* The checkstyle grader uses `maven-checkstyle-plugin`.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/dscpsyl/jgrade2.svg?style=for-the-badge
[contributors-url]: https://github.com/dscpsyl/jgrade2/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/dscpsyl/jgrade2.svg?style=for-the-badge
[forks-url]: https://github.com/dscpsyl/jgrade2/network/members
[stars-shield]: https://img.shields.io/github/stars/dscpsyl/jgrade2.svg?style=for-the-badge
[stars-url]: https://github.com/dscpsyl/jgrade2/stargazers
[issues-shield]: https://img.shields.io/github/issues/dscpsyl/jgrade2.svg?style=for-the-badge
[issues-url]: https://github.com/dscpsyl/jgrade2/issues
[license-shield]: https://img.shields.io/github/license/dscpsyl/jgrade2.svg?style=for-the-badge
[license-url]: https://github.com/dscpsyl/jgrade2/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/davidjsim/
