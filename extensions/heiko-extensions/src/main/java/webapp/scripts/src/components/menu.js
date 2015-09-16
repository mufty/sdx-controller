var Menu = React.createClass({
	menuItems: [{
		name: "Post maker"
	}],
	handleClick: function(e) {
		$(UI).trigger('setState', 'post_maker');
	},
	render: function(){
		return (<div className='menu'>
			{this.menuItems.map(function(item) {
	          return <a onClick={this.handleClick.bind(this)}>{item.name}</a>;
	        }, this)}
		</div>);
	}
});