/**
 * 
 */

var UI = {};

var App = React.createClass({
	getInitialState: function() {
	    return {appState: 'init'};
	},
	componentDidMount: function(){
	    $(UI).on('setState', function(e, stateName){
	      this.setState({appState: stateName});
	    }.bind(this));
	},
	componentWillUnmount: function () {
	    $(UI).off('setState');
	},
	render: function(){
		var appState;
		if(this.state.appState == 'init'){
			appState = "TODO";
		} else if (this.state.appState == 'post_maker'){
			appState = <PostMaker />;
		} else {
			appState = "";
		}
		return (<div>
			<Menu/>
			{appState}
		</div>);
	}
});
	
React.render(
		<App/>,
		document.getElementById('container')
);